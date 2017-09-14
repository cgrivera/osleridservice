from django.db import models
from . import utils

class OslerId(models.Model):
    oslerid = models.CharField(max_length=25)

    def __string__(self):
        return self.oslerid



