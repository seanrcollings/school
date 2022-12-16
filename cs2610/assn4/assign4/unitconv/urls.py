from django.urls import path

from .views import convert

urlpatterns = [path("convert", convert, name="convert")]
