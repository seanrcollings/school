import typing as t
import collections
import jsonschema
import json

from producer import errors

WIDGET_SCHEMA = {
    "$schema": "http://json-schema.org/draft-04/schema#",
    "type": "object",
    "properties": {
        "type": {"type": "string", "pattern": "create|update|delete"},
        "requestId": {"type": "string"},
        "widgetId": {"type": "string"},
        "owner": {"type": "string", "pattern": "[A-Za-z ]+"},
        "label": {"type": "string"},
        "description": {"type": "string"},
        "otherAttributes": {
            "type": "array",
            "items": [
                {
                    "type": "object",
                    "properties": {
                        "name": {"type": "string"},
                        "value": {"type": "string"},
                    },
                    "required": ["name", "value"],
                }
            ],
        },
    },
    "required": ["type", "requestId", "widgetId", "owner"],
}


class WidgetRequest(collections.UserDict):
    @classmethod
    def from_json(cls, data: str):
        body = cls.__parse_request(data)
        cls.__validate_request(body)
        body = cls.__transform_request(body)
        return cls(body)

    @classmethod
    def __parse_request(self, content: str):
        try:
            return json.loads(content)
        except json.JSONDecodeError as e:
            raise errors.RequestError(400, str(e)) from e

    @classmethod
    def __validate_request(self, request: t.Dict[str, t.Any]):
        try:
            jsonschema.validate(request, WIDGET_SCHEMA)
        except jsonschema.ValidationError as e:
            raise errors.RequestError(400, e.message) from e

    # I don't think this acutally needs to do anything yet
    @classmethod
    def __transform_request(self, request):
        return request
