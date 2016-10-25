from cv2 import imread, imwrite, resize, imshow, waitKey
import numpy as np
import json
import base64
import os.path
from os import listdir
from os.path import isfile, join

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


def preprocess_img(dir_from, dir_to):
    files = [f for f in listdir(dir_from) if isfile(join(dir_from, f))]
    for f in files:
        resize_img(join(dir_from, f), dir_to)


def img_to_base64(img_path):
    with open(img_path, "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read())
    encoded_string = encoded_string.decode("utf-8")
    return encoded_string

if __name__ == "__main__":
    preprocess_img('img/non-resized', 'img/')
