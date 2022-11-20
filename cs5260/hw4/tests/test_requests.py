import pytest
import os
import json
from lambda_function import handle
import boto3_mocking  # type: ignore

boto3_mocking.engage_patching()


good_requests = os.listdir("sample-requests/good")


@pytest.mark.parametrize("file", good_requests)
def test_successful_requests(file):
    with open(f"sample-requests/good/{file}") as f:
        contents = f.read()

    response = handle({"body": contents}, None)
    assert response["body"] == '{"success": true}'


bad_requests = os.listdir("sample-requests/bad")


@pytest.mark.parametrize("file", bad_requests)
def test_unsuccessful_requests(file):
    with open(f"sample-requests/bad/{file}") as f:
        contents = f.read()

    response = handle({"body": contents}, None)
    assert response["statusCode"] == 400
