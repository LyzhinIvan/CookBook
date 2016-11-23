#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tornado.ioloop
import tornado.web
import models
# from server import models
import json
import peewee
import sys
import os
from datetime import datetime

# ======================================== Aliases
RECIPES_FIELD_NAME = 'recipes'
CATEGORIES_FIELD_NAME = 'categories'
INGREDIENTS_FIELD_NAME = 'ingredients'
RECIPE_INGREDIENTS_FIELD_NAME = 'recipeIngredients'
NEW_UPDATED_FIELD_NAME = 'newUpdated'
# ========================================


def get_new_records(last_updated):
    result = {CATEGORIES_FIELD_NAME: [], INGREDIENTS_FIELD_NAME: [], RECIPES_FIELD_NAME: [],
              RECIPE_INGREDIENTS_FIELD_NAME: [],
              NEW_UPDATED_FIELD_NAME: int(datetime.timestamp(datetime.now()))}
    try:

        """ Adding recipes """
        select_result = models.Recipe.select().where(models.Recipe.timestamp_added > last_updated)
        recipes = []

        for s in select_result:
            if s.id is None:
                continue
            tmp_rec = {}
            tmp_rec['id'] = s.id
            tmp_rec['name'] = s.name
            tmp_rec['icon'] = s.picture.decode("utf-8")
            tmp_rec['time'] = s.time
            tmp_rec['instruction'] = s.instruction
            tmp_rec['category'] = s.category.id
            recipes.append(tmp_rec)
        result[RECIPES_FIELD_NAME] = recipes

        recipes = [r['id'] for r in recipes]
        """ Adding recipe-ingredients connections """
        select_result = models.RecipeIngredient.select()
        select_result = models.RecipeIngredient.select().where(models.RecipeIngredient.recipe_id in recipes)
        rec_ing = []

        for s in select_result:
            if s.id is None:
                continue
            tmp_rec_ing = {}
            tmp_rec_ing['id'] = s.id
            tmp_rec_ing['resId'] = s.recipe_id
            tmp_rec_ing['ingId'] = s.ingredient_id
            tmp_rec_ing['quantity'] = s.quantity
            rec_ing.append(tmp_rec_ing)
        result[RECIPE_INGREDIENTS_FIELD_NAME] = rec_ing
        del rec_ing

    except peewee.DoesNotExist:
        pass

    try:
        """ Adding categories """
        select_result = models.Category.select().where(models.Category.timestamp_added > last_updated)
        categories = []

        for s in select_result:
            if s.id is None:
                continue
            tmp_cat = {}
            tmp_cat['id'] = s.id
            tmp_cat['name'] = s.name
            categories.append(tmp_cat)
        result[CATEGORIES_FIELD_NAME] = categories
        del categories
    except peewee.DoesNotExist:
        pass

    try:
        """Adding ingredients """
        select_result = models.Ingredient.select().where(models.Ingredient.timestamp_added > last_updated)
        ingredients = []

        for s in select_result:
            if s.id is None:
                continue
            tmp_ing = {}
            tmp_ing['id'] = s.id
            tmp_ing['name'] = s.name
            ingredients.append(tmp_ing)
        result[INGREDIENTS_FIELD_NAME] = ingredients
        del ingredients
    except peewee.DoesNotExist:
        pass

    return result


class MainHandler(tornado.web.RequestHandler):
    def data_received(self, chunk):
        pass

    def get(self):
        self.write("Hello, world\n")


class DeltaHandler(tornado.web.RequestHandler):
    def data_received(self, chunk):
        pass

    def get(self):
        last_updated = self.get_argument('lastUpdated', None)

        if not last_updated:
            self.write('Invalid arguments')
            return

        try:
            last_updated = int(last_updated)
        except ValueError:
            self.write('Invalid arguments')
            return
        try:
            result = json.dumps(get_new_records(last_updated), ensure_ascii=False).encode('utf8')
            result_size = sys.getsizeof(result)
            result = {'delta': result_size / 1048576}  # result in MB
        except peewee.DoesNotExist:
            result = {'delta': -1}

        self.write(json.dumps(result))

class UpdateHandler(tornado.web.RequestHandler):
    def data_received(self, chunk):
        pass

    def get(self):
        last_updated = self.get_argument('lastUpdated', None)
        if not last_updated:
            self.write('Invalid arguments')
            return

        try:
            last_updated = int(last_updated)
        except ValueError:
            self.write('Invalid arguments')
            return
        result = json.dumps(get_new_records(last_updated), ensure_ascii=False).encode('utf8')
        self.write(result)


def make_app():
    return tornado.web.Application([
        (r'/', MainHandler),
        (r'/api/delta/?', DeltaHandler),
        (r'/api/update/?', UpdateHandler)
    ])


if __name__ == '__main__':
    print("Tornado works")
    app = make_app()
    app.listen(8800)
    # app.listen(int(os.environ['PORT'])) # if you're deploying it on heroku cloud
    tornado.ioloop.IOLoop.current().start()
