#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tornado.ioloop
import tornado.web
import models
# from server import models
import json
import peewee
import sys


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
            new_recipes = models.TimeAdded.get(models.TimeAdded.time_added > last_updated)
            result_size = sys.getsizeof(json.dumps(new_recipes))
            result = {'delta': result_size / 1024}  # result in MB
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

        try:
            new_recipes = models.TimeAdded.get(models.TimeAdded.time_added > last_updated)
            result = json.dumps(new_recipes)
        except peewee.DoesNotExist:
            result = {}

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
