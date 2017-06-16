from django.views.generic import View
from django.contrib.auth.mixins import LoginRequiredMixin
from django.http import HttpResponse
from django.template.loader import render_to_string
from django.shortcuts import redirect
from rest_framework import generics, permissions
from rest_framework.exceptions import ValidationError
from rest_framework.response import Response

from . import api
from . import models
from . import serializers
from . import forms
from . import utils
from .exceptions import UpdateError

class HttpResponseNoContent(HttpResponse):
    status_code = 204

# DP NOTE: For site wide enabling of LoginRequired see
# https://stackoverflow.com/questions/2164069/best-way-to-make-djangos-login-required-the-default

# DP NOTE: APIViews called by Console Views do not check that the user is authenticated

# DP NOTE: To restrict data to what the logged in user can see change how the queryset is obtained
# http://www.django-rest-framework.org/api-guide/filtering/#filtering-against-the-current-user


### REST API Views ###

class OslerIdList(generics.ListCreateAPIView):
    permission_classes = (permissions.IsAuthenticated, )
    queryset = models.OslerId.objects.all()
    serializer_class = serializers.OslerIdSerializer

    def list(self, request):
        # Note the use of `get_queryset()` instead of `self.queryset`
        queryset = utils.oslerid_generator(request.GET['next'])
        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)




## Helper Methods ##
from rest_framework import status
# DP TODO: create a common base method
def _get(cls, request, *args, **kwargs):
    inst = cls()
    inst.request = request
    inst.kwargs = kwargs
    inst.format_kwarg = inst.serializer_class
    resp = inst.get(request, *args, **kwargs)
    if not status.is_success(resp.status_code):
        return None
    else:
        return resp.data

def _post(cls, request, data, *args, **kwargs):
    inst = cls()
    inst.request = request
    inst.kwargs = kwargs
    inst.format_kwarg = inst.serializer_class
    if data:
        inst.request.data = data
    resp = inst.post(request, *args, **kwargs)
    if not status.is_success(resp.status_code):
        return None

def _put(cls, request, data, *args, **kwargs):
    inst = cls()
    inst.request = request
    inst.kwargs = kwargs
    inst.format_kwarg = inst.serializer_class
    if data:
        inst.request.data = data
    resp = inst.put(request, *args, **kwargs)
    if not status.is_success(resp.status_code):
        return None

def _delete(cls, request, *args, **kwargs):
    inst = cls()
    inst.request = request
    inst.kwargs = kwargs
    inst.format_kwarg = inst.serializer_class
    resp = inst.delete(request, *args, **kwargs)
    if not status.is_success(resp.status_code):
        return None



### Console Views ###
class OslerIdNext(LoginRequiredMixin, View):
    def get(self, request, form=None):
        if form is None:
            form = forms.OslerIdNextForm()
        args = {
            'url_name': 'oslerid',
            'form': form,
            'items': _get(OslerIdList, request),

        }
        return HttpResponse(render_to_string('list.html', args, request))

    def post(self, request):
        form = forms.OslerIdNextForm(request.POST)
        if form.is_valid():
            data = form.cleaned_data.copy()

            try:
                _post(OslerIdList, request, data)

                return redirect('oslerid')
            except ValidationError as e:
                for k, v in e.detail.items():
                    if k == 'non_field_errors':
                        k = None
                    form.add_error(k, v)

        return self.get(request, form)