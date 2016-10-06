#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tornado.ioloop
import tornado.web
import models


class MainHandler(tornado.web.RequestHandler):
	def get(self):
		self.write("Hello, world\n")


class RecipesHandler(tornado.web.RequestHandler):
	def get(self):
		response = { 'recipes': [
			{'id':			1,
			 'name':		'Макарошки',
			 'instruction': '1) Вскипятить воду\n2) Посолить\n3) Закинуть макарошки\n4) Варить до готовности'},
			{'id':			2,
			 'name':		'Пельмешки',
			 'instruction': '1) Вскипятить воду\n2) Посолить\n3) Закинуть пельмешки\n4) Варить до готовности'}
		]}
		self.write(response)


class RecipeHandler(tornado.web.RequestHandler):
	def get(self, id):
		response = { 'id':			int(id),
					 'name':		'Макарошки',
					 'instruction': '1) Вскипятить воду\n2) Посолить\n3) Закинуть макарошки\n4) Варить до готовности'}
		self.write(response)


def make_app():
	return tornado.web.Application([
		(r'/', MainHandler),
		(r'/api/recipes/?', RecipesHandler),
		(r'/api/recipes/([0-9]+)/?', RecipeHandler),
	])

if __name__ == '__main__':
	app = make_app()
	app.listen(8888)
	tornado.ioloop.IOLoop.current().start()