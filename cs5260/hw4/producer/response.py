import typing as t
import json


def ok(body: dict[str, t.Any]):
    return format_response(200, body)


def error(body: dict[str, t.Any], code: int = 400):
    return format_response(code, body)


def format_response(code: int, body: dict[str, t.Any]):
    return {
        "statusCode": code,
        "headers": {"Content-Type": "application/json"},
        "isBase64Encoded": False,
        "body": json.dumps(body),
    }
