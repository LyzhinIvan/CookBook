#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tornado.ioloop
import tornado.web
import models
# from server import models
import json
import peewee
import sys
from datetime import datetime


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
            new_recipes = models.Recipe.get(models.Recipe.timestamp_added > last_updated)
            result_size = sys.getsizeof(json.dumps(new_recipes))
            result = {'delta': result_size / 1024}  # result in MB
        except peewee.DoesNotExist:
            result = {'delta': -1}

        self.write(json.dumps(result))

# ======================================== Aliases
RECIPES_FIELD_NAME = 'recipes'
CATEGORIES_FIELD_NAME = 'categories'
INGREDIENTS_FIELD_NAME = 'ingredients'
RECIPE_INGREDIENTS_FIELD_NAME = 'recipeIngredients'
NEW_UPDATED_FIELD_NAME = 'newUpdated'
# ========================================


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

        try:
            result = {}

            select_result = models.Category.get(models.Category.timestamp_added > last_updated)
            # TODO: выпилить из categories timestamp
            result[CATEGORIES_FIELD_NAME] = json.dumps(select_result)

            select_result = models.Ingredient.get(models.Ingredient.timestamp_added >last_updated)
            # TODO: выпилить из ingredients timestamp
            result[INGREDIENTS_FIELD_NAME] = json.dumps(select_result)

            select_result = models.Recipe.get(models.Recipe.timestamp_added > last_updated)
            # TODO: выпилить из recipes timestamp
            result[RECIPES_FIELD_NAME] = json.dumps(select_result)

            select_result = models.RecipeIngredient.select().join(models.Recipe)\
                .where(models.Recipe.timestamp_added > last_updated)
            # TODO: отбросить лишние поля
            result[RECIPE_INGREDIENTS_FIELD_NAME] = json.dumps(select_result)

            result[NEW_UPDATED_FIELD_NAME] = int(datetime.timestamp(datetime.now()))
            result = json.dumps(result)
        except peewee.DoesNotExist:
            result = {CATEGORIES_FIELD_NAME: [], INGREDIENTS_FIELD_NAME: [], RECIPES_FIELD_NAME: [],
                      RECIPE_INGREDIENTS_FIELD_NAME: [],
                      NEW_UPDATED_FIELD_NAME: int(datetime.timestamp(datetime.now()))}

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
    app.listen(8888)
    tornado.ioloop.IOLoop.current().start()
