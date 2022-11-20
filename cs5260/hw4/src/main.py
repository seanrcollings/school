import typing as t

import boto3
import jsonschema
import json

import response

sqs = boto3.resource("sqs")


class LambdaContext(t.Protocol):
    function_name: str
    function_version: str
    invoked_function_arn: str
    memory_limit_in_mb: int
    aws_requed_id: str
    log_group_name: str
    identity: str

    def get_remaining_time_in_millis(self) -> int:
        ...


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


class RequestError(Exception):
    def __init__(self, code: int, message: str):
        self.code = code
        self.message = message


def handle(event: t.Dict[str, t.Any], context: LambdaContext):
    try:
        body = parse_request(event["body"])
        validate_request(body)
    except RequestError as e:
        return response.error({"error": e.message}, e.code)


def parse_request(content: str):
    try:
        return json.dumps(content)
    except json.JSONDecodeError as e:
        raise RequestError(400, str(e)) from e


def validate_request(request: t.Dict[str, t.Any]):
    try:
        jsonschema.validate(request, WIDGET_SCHEMA)
    except jsonschema.ValidationError as e:
        raise RequestError(400, e.message) from e


def transform_request():
    ...
