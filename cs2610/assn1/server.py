from typing import Type
from http.server import HTTPServer, BaseHTTPRequestHandler  # type: ignore
from pathlib import Path
import datetime
from utils import Router


# REDIRECTS = {
#     "/about.html": {
#         "bio",
#         "bio.html",
#         "bioo",
#         "biooo",
#         "bioooo",
#         "biography",
#         "biopic",
#         "biohazard",
#         "biology",
#         "biopsy",
#     },
#     "/techtips+css.html": {"help", "tips"},
# }


# class RequestHandler(BaseHTTPRequestHandler):
#     CONTENT_TYPE_MAPPING = {
#         "html": "text/html",
#         "css": "text/css",
#         "jpg": "image/jpeg",
#         "jpeg": "image/jpeg",
#         "png": "image/png",
#     }

#     def do_GET(self):
#         path: Path = Path(self.path.lstrip("/"))

#         route = str(path)
#         if route == "favicon.ico":
#             path = Path("static/img/favicon.ico")
#         elif route == "debugging":
#             self.send_response(200)
#             self.send_header("Content-type", "text/plain")
#             self.end_headers()
#             self.wfile.write(
#                 (
#                     f"VERSION: {self.server_version} \n"
#                     f"TIME: {datetime.datetime.now()} \n"
#                     f"IP: {self.client_address[0]}:{self.client_address[1]} \n"
#                     f"PATH {self.path}\n"
#                     f"HTTP REQUEST TYPE: {self.command} \n"
#                     f"HTTP VERSION: {self.request_version} \n"
#                     f"REQUEST HEADERS: {','.join(self.headers)}"
#                 ).encode("utf-8")
#             )
#             return
#         elif route == "teapot":
#             teapot_path = Path("pages/teapot.html")
#             self.general_headers(teapot_path, 418)
#             self.end_headers()
#             self.read_file(teapot_path)
#             return
#         elif route == "forbidden":
#             self.send_response(403, "Forbidden")
#             self.end_headers()
#             self.read_file(Path("pages/403.html"))
#             return

#         if len(path.parts) > 0 and path.parts[0] == "static":
#             self.handle_static(path)
#         else:
#             if str(path) == ".":
#                 path = Path("index.html")

#             self.handle_pages(path)

#     def general_headers(self, path: Path, code: int = 200):
#         self.send_response(code)
#         self.send_header(
#             "Content-type",
#             self.CONTENT_TYPE_MAPPING.get(path.suffix.strip("."), "text/html"),
#         )
#         self.send_header("Connection", "close")
#         self.send_header("Cache-Control", "max-age=1")
#         with path.open("rb") as file:
#             self.send_header("Content-Length", str(len(file.read())))

#     def handle_static(self, path: Path):
#         if not path.is_file():
#             self.not_found()
#         else:
#             self.general_headers(path)
#             self.end_headers()
#             self.read_file(path)

#     def handle_pages(self, path: Path):
#         if not (Path("pages") / path).is_file():
#             if self.handle_redirects(path):
#                 self.end_headers()
#             else:
#                 self.not_found()
#         else:
#             path = Path("pages") / path
#             self.general_headers(path)
#             self.end_headers()
#             self.read_file(path)

#     def handle_redirects(self, path: Path):
#         redirect_path = None

#         if path.suffix != ".html":
#             redirect_path = self.path + ".html"

#         for to, paths in REDIRECTS.items():
#             if str(path) in paths:
#                 redirect_path = to

#         if redirect_path:
#             self.send_response(301)
#             self.send_header("Location", redirect_path)
#             return True

#         return False

#     def read_file(self, path: Path):
#         with path.open("rb") as file:
#             self.wfile.write(file.read())

#     def not_found(self):
#         self.send_error(404, "File not Found")


# def run(
#     server_class: Type[HTTPServer] = HTTPServer,
#     handler_class: Type[BaseHTTPRequestHandler] = BaseHTTPRequestHandler,
# ):
#     server_address = ("", 8000)
#     httpd = server_class(server_address, handler_class)
#     httpd.serve_forever()


# run(handler_class=RequestHandler)


router = Router()


class RequestHandler(BaseHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        self.router = router
        super().__init__(*args, **kwargs)

    def do_GET(self):
        ...
