from rest_framework import serializers
from drf_writable_nested import WritableNestedModelSerializer

from . import models
from . import utils
from .exceptions import UpdateError

def check(validated_data, key, value):
    if value != validated_data.get(key, value):
        raise UpdateError("Cannot update '{}' field".format(key))

class OslerIdSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.OslerId
        fields= ['oslerid']

