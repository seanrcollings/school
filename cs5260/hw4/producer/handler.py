import typing as t
import os
import json

import boto3
from producer.widget import WidgetRequest

sqs = boto3.resource("sqs", "us-east-1")


class SQSHandler:
    def __init__(self, queue_url: str):
        self.queue = sqs.Queue(queue_url)

    def handle(self, request: WidgetRequest):
        self.queue.send_message(MessageBody=json.dumps(dict(request)))
