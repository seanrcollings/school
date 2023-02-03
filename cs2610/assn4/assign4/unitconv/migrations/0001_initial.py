# Generated by Django 3.2 on 2021-04-15 23:15

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Conversion',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('to_unit', models.CharField(max_length=100)),
                ('from_unit', models.CharField(max_length=100)),
                ('conversion_factor', models.IntegerField()),
            ],
        ),
    ]