#!/usr/bin/env python
# -*- coding: utf-8 -*-

from peewee import *

db = SqliteDatabase('cookbook.db')


class BaseModel(Model):
    class Meta:
        database = db


class Ingredient(BaseModel):
    name = CharField(unique=True)  # Названия рецепта
    timestamp_added = IntegerField()  # Время добавления ингредиента в базу

    class Meta:
        db_table = 'ingredients'


class Category(BaseModel):
    name = CharField(unique=True)  # Названия категории
    timestamp_added = IntegerField()  # Время добавления категории в базу

    class Meta:
        db_table = 'categories'


class Recipe(BaseModel):
    """ Название рецепта. ms_lilibeth: сделала не уникальным -- не вижу смысла. У того же плова тысячи рецептов,
    и это все -- плов """
    name = CharField()
    picture = BlobField()  # Картинка к рецепту
    time = IntegerField()  # Время приготовления в минутах
    instruction = TextField()  # Инструкция по приготовлению
    category = ForeignKeyField(Category)  # Категория рецепта
    timestamp_added = IntegerField()  # Время добавления рецепта в базу

    class Meta:
        db_table = 'recipes'


class RecipeIngredient(BaseModel):
    recipe = ForeignKeyField(Recipe, related_name='ingredients')  # Ссылка на рецепт
    ingredient = ForeignKeyField(Ingredient)  # Ссылка на ингредиент
    quantity = CharField()

    class Meta:
        db_table = 'recipe_ingredients'


def create_tables():
    db.connect()
    db.create_tables([Ingredient, Category, Recipe, RecipeIngredient],
                     safe=True)  # safe=True для того, чтобы таблицы не пересоздавались

if __name__ == '__main__':
    create_tables()
