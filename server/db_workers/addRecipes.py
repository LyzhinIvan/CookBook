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
        self.ingredients_available = []
        self.categories_available = []
        # TODO: read ingredients & categories from db
        # TODO: how to identify recipes?
        pass

    def preprocess_title(self, text):
        text = str.join(" ", text.split())  # Убираем лишние пробелы
        return text

    # Removes redundant spaces, converts to lowercase, raises ValueError if not in ingredients_available
    def preprocess_ingredient(self, text):
        pass

    # converts into int else raises ValueError
    def preprocess_cook_time(self, text):
        pass

    # Removes redundant spaces, convert to lowercase, raise ValueError if not in categories_available
    def preprocess_category(self, text):
        pass

    ''' Reads the picture, converts to base64. Raises OSError if file not found'''
    def preprocess_img(self, path):
        pass

    def preprocess_instruction(self, text):
        return text

def add_recipes(xml_filepath):
    log_filepath = 'db.log'

    tree = ET.parse(xml_filepath)
    root = tree.getroot()
    try:
        select_result = models.Recipe.select()
        select_result = [i.name for i in select_result]
    except DoesNotExist:
        select_result = []

    count_insertions = 0
    inserted = []
    if not select_result:
        for recipe in root:
            if recipe.tag != "recipe":
                continue
            for item in recipe:

                name = str.join(" ", recipe.text.split())  # Убираем лишние пробелы
                name = str.lower(name)
                i = models.Ingredient(name=name, timestamp_added=int(datetime.timestamp(datetime.now())))
                i.save()
                count_insertions += 1
                inserted.append(name)
    else:
        for recipe in root:
            if recipe.text is None or recipe.text in select_result:
                continue
            name = str.join(" ", recipe.text.split())  # Убираем лишние пробелы
            name = str.lower(name)
            i = models.Ingredient(name=name, timestamp_added=int(datetime.timestamp(datetime.now())))
            i.save()
            count_insertions += 1
            inserted.append(name)
if __name__ == "__main__":
    # preprocess_img('img/non-resized', 'img/')
    add_recipes("recipes.xml")