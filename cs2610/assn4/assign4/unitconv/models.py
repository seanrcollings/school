from django.db import models

# In retrospect, the to_unit is un-needed because it will alway be the same, but eh


class Conversion(models.Model):
    to_unit = models.CharField(max_length=100)
    from_unit = models.CharField(max_length=100)
    conversion_factor = models.FloatField()
