class Router:
    def __init__(self):
        self.routes = []

    def route(self, path: str):
        def decorator(func):
            self.routes.append({"path": path, "func": func})
            return func

        return decorator


class Render:
    def __init__(self, filename):
        self.filename = filename


class Redirect:
    def __init__(self, url):
        self.url = url
