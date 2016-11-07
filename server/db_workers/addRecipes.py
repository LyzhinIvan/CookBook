from cv2 import imread, imwrite, resize, imshow, waitKey
import numpy as np
import json
import base64
import os.path
from datetime import datetime
from os import listdir
from os.path import isfile, join
import xml.etree.ElementTree as ET
from peewee import *
from difflib import SequenceMatcher
from server.db_workers import models
# import models


RESIZE_TO = 200
RECIPE_TAG = "recipe"
NAME_TAG = "title"
CATEGORY_TAG = "category"
COOK_TIME_TAG = "cook_time"
PICTURE_TAG = "picture_filepath"
INGREDIENT_TAG = "ingredient"
INSTRUCTION_TAG = "instruction"


def resize_img(filepath_from, dir_to):
    print("Resizing " + filepath_from + "...")
    img = imread(filepath_from)
    min_size = min(img.shape[0], img.shape[1])
    if min_size > RESIZE_TO:
        k = RESIZE_TO/min_size
        img = resize(img, None, fx=k, fy=k)
    filepath_to = join(dir_to, os.path.basename(filepath_from))
    imwrite(filepath_to, img)
    print("Saved to " + filepath_to)


def preprocess_all_imgs(dir_from, dir_to):
    files = [f for f in listdir(dir_from) if isfile(join(dir_from, f))]
    for f in files:
        resize_img(join(dir_from, f), dir_to)


def img_to_base64(img_path):
    with open(img_path, "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read())
    encoded_string = encoded_string.decode("utf-8")
    return encoded_string


def text_similarity_percent(str1, str2):
    return SequenceMatcher(None, str1, str2).ratio()


# Class for preprocessing items of recipe before writing them to database
class PreprocessItem:
    def __init__(self):
        select_res = models.Ingredient.select()
        self.ingredients_available = [i.name for i in select_res]
        select_res = models.Category.select()
        self.categories_available = [i.name for i in select_res]
        select_res = models.Recipe.select()
        # recipes are identified by their instructions
        self.recipes_available = [i.instruction for i in select_res]

    def preprocess_title(self, text):
        text = str.join(" ", text.split())  # Убираем лишние пробелы
        return text

    # Removes redundant spaces, converts to lowercase, raises ValueError if not in ingredients_available
    def preprocess_ingredient(self, text):
        text = str.join(" ", text.split()).lower()
        return text

    # converts into int else raises ValueError
    @staticmethod
    def preprocess_cook_time(text):
        return int(text)

    # Removes redundant spaces, convert to lowercase, raise ValueError if not in categories_available
    def preprocess_category(self, text):
        text = str.join(" ", text.split()).lower()
        if text not in self.categories_available:
            raise ValueError("Unknown category")
        return text

    ''' Reads the picture, converts to base64. Raises OSError if file not found'''
    def preprocess_img(self, path):
        return img_to_base64(path)

    def preprocess_instruction(self, text):
        for i in self.recipes_available:
            if text_similarity_percent(text, i) >= 0.9:
                raise ValueError("Text similarity percent >= 0.9")
        return text


def add_recipes(xml_filepath):
    log_filepath = 'db.log'
    preprocessor = PreprocessItem()
    tree = ET.parse(xml_filepath)
    root = tree.getroot()
    try:
        select_result = models.Recipe.select()
        select_result = [i.name for i in select_result]
    except DoesNotExist:
        select_result = []

    count_insertions = 0
    inserted = []

    for recipe in root:
        if recipe.tag != RECIPE_TAG:
            continue
        ingredient_list = []
        try:
            for item in recipe:
                if item.tag == NAME_TAG:
                    title = preprocessor.preprocess_title(item.text)
                    continue
                if item.tag == CATEGORY_TAG:
                    category = preprocessor.preprocess_category(item.text)
                    continue
                if item.tag == COOK_TIME_TAG:
                    time = preprocessor.preprocess_cook_time(item.text)
                    continue
                if item.tag == PICTURE_TAG:
                    picture = preprocessor.preprocess_img(item.text)
                    continue
                if item.tag == INGREDIENT_TAG:
                    ingredient_list.append(preprocessor.preprocess_ingredient(item.text))
                    continue
                if item.tag == INSTRUCTION_TAG:
                    instruction = preprocessor.preprocess_instruction(item.text)
                    continue
        except ValueError:
            continue

        new_recipe = models.Recipe(name=title, timestamp_added=int(datetime.timestamp(datetime.now())))
        new_recipe.category = category
        new_recipe.time = time
        new_recipe.picture = picture
        new_recipe.instruction = instruction
        new_recipe.save()
        count_insertions += 1
        inserted.append(title)

        ingredient_list = [models.Ingredient.get(models.Ingredient.name == i) for i in ingredient_list]
        for i in ingredient_list:
            rec_ing = models.RecipeIngredient(recipe = new_recipe, ingredient = i)
            rec_ing.save()


if __name__ == "__main__":
    # preprocess_img('img/non-resized', 'img/')
    add_recipes("recipes.xml")