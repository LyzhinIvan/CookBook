#!/usr/bin/env python
# -*- coding: utf-8 -*-

from peewee import *

db = SqliteDatabase('cookbook.db')

class BaseModel(Model):
	class Meta:
		database = db

class Ingredient(BaseModel):
	name = CharField(unique=True)			# Названия рецепта
	class Meta:
		db_table = 'ingredients'

class Category(BaseModel):
	name = CharField(unique=True)			# Названия категории
	class Meta:
		db_table = 'categories'

class Recipe(BaseModel):
	name = CharField(unique=True)			# Название рецепта
	picture = BlobField()					# Картинка к рецепту
	time = IntegerField()					# Время приготовления в минутах
	instruction = TextField()				# Инструкция по приготовлению
	category = ForeignKeyField(Category)	# Категория рецепта
	class Meta:
		db_table = 'recipes'

class RecipeIngredient(BaseModel):
	recipe = ForeignKeyField(Recipe, related_name='ingredients')	# Ссылка на рецепт
	ingredient = ForeignKeyField(Ingredient)						# Ссылка на игредиент
	class Meta:
		db_table = 'recipe_ingredients'

def create_tables():
	db.connect()
	db.create_tables([Ingredient, Category, Recipe, RecipeIngredient], safe=True)	# safe=True для того, чтобы таблицы не пересоздавались