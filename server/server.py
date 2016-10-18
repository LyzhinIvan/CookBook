#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tornado.ioloop
import tornado.web
import models
import json
from datetime import datetime  # перевод из timestamp - datetime.fromtimestamp
import peewee


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

        self.write('result (delta in Mb) will be there')


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
        
        self.write('select result will be there')


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
