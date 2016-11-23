import xml.etree.ElementTree as ET
from peewee import *
# import models
import models
from datetime import datetime

log_filepath = 'db.log'

tree = ET.parse('ingredients.xml')
root = tree.getroot()
try:
    select_result = models.Ingredient.select()
    select_result = [i.name for i in select_result]
except DoesNotExist:
    select_result = []

count_insertions = 0
inserted = []
if not select_result:
    for child in root:
        if child.text is not None:
            name = str.join(" ", child.text.split())  # Убираем лишние пробелы
            name = str.lower(name)
            i = models.Ingredient(name=name, timestamp_added=int(datetime.timestamp(datetime.now())))
            i.save()
            count_insertions += 1
            inserted.append(name)
else:
    for child in root:
        if child.text is None or child.text in select_result:
            continue
        name = str.join(" ", child.text.split())  # Убираем лишние пробелы
        name = str.lower(name)
        i = models.Ingredient(name=name, timestamp_added=int(datetime.timestamp(datetime.now())))
        i.save()
        count_insertions += 1
        inserted.append(name)

with open(log_filepath, 'a') as f:
    msg = "***************************\n"
    msg = datetime.now().strftime('%d/%m/%Y %H:%M') + ' Inserted %d row(s)' % count_insertions + '\n'
    msg += "=========== Inserted: \n"
    msg += str.join("\n", [i for i in inserted])
    msg += "======================"
    f.write(msg)
