import typing as t
import os
import logging

from producer import response
from producer import widget
from producer import errors
from producer.handler import SQSHandler


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


def handle(event: t.Dict[str, t.Any], context: LambdaContext):
    logging.info("Recieved request")
    try:
        queue_url = os.getenv("SQS_URL")
        if not queue_url:
            logging.error("no queue configuration")
            return response.error({"error": "no queuue configured"})

        handler = SQSHandler(queue_url)
        body = event["body"]
        if not body:
            return response.error({"error": "no body"})

        widget_request = widget.WidgetRequest.from_json(body)
        handler.handle(widget_request)
        logging.info("Created request successfully")
        return response.ok({"success": True})
    except errors.RequestError as e:
        logging.error(e.message)
        return response.error({"error": e.message}, e.code)
