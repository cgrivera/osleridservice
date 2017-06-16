"""projection URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from django.conf.urls import include

from . import views

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^api-admin/', include('rest_framework.urls', namespace='rest_framework')),

    # DP ???: Version the API?

    ### REST API ###


    url(r'^oslerid/', views.OslerIdList.as_view()),
    url(r'^oslerid/(?P<pk>[0-9]+)/', views.OslerIdList.as_view()),


    ### Console ###


    url(r'^console/oslerid/', views.OslerIdNext.as_view()),
    url(r'^console/oslerid/(?P<pk>[0-9]+)/', views.OslerIdNext.as_view()),
]
