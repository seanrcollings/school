from django.shortcuts import render
from django.http import HttpRequest, JsonResponse

from .models import Conversion


error_response = JsonResponse({"error": "Invalid unit conversion request"})


BASE_UNIT = "kg"


def convert(request: HttpRequest):
    to_unit = request.GET.get("to", "")
    from_unit = request.GET.get("from", "")
    value = request.GET.get("value", "")

    if not value or not to_unit or not from_unit:
        return error_response

    try:
        parsed = float(value)
    except ValueError:
        return error_response

    # Convert the provided value into the base of Kilograms
    base_conversion = Conversion.objects.get(from_unit=from_unit, to_unit=BASE_UNIT)
    kilograms: float = parsed * base_conversion.conversion_factor
    if to_unit == BASE_UNIT:
        converted_value = kilograms
    else:
        # Convert the kilograms to the requested unit
        conversion = Conversion.objects.get(from_unit=to_unit, to_unit=BASE_UNIT)
        inverse: float = 1 / conversion.conversion_factor
        converted_value = kilograms * inverse

    response = JsonResponse({"units": to_unit, "value": converted_value})
    response["Access-Control-Allow-Origin"] = "*"
    return response
