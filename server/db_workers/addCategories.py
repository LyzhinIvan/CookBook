import xml.etree.ElementTree as ET
from peewee import *
# import models
from server.db_workers import models
from datetime import datetime

log_filepath = 'db.log'

tree = ET.parse('categories.xml')
root = tree.getroot()
try:
    select_result = models.Category.select()
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
            i = models.Category(name=name, timestamp_added=int(datetime.timestamp(datetime.now())))
            i.save()
            inserted.append(name)
            count_insertions += 1
else:
    for child in root:
        if child.text is None:
            continue
        name = str.join(" ", child.text.split())  # Убираем лишние пробелы
        name = str.lower(name)
        if name in select_result:
            continue
        i = models.Category(name=name, timestamp_added=int(datetime.timestamp(datetime.now())))
        i.save()
        inserted.append(name)
        count_insertions += 1

with open(log_filepath, 'a') as f:
    msg = datetime.now().strftime('%d/%m/%Y %H:%M') + ' Inserted %d row(s)' % count_insertions + '\n'
    f.write(msg)
    msg = "=========== Inserted: \n"
    msg += str.join("\n", [i for i in inserted])
    msg += "======================"
    f.write(msg)
