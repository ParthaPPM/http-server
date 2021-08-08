# Slow-Server
This is my http server program which can be used with another program which requires a (simple) server implementation.

**How to use:-**

1. To start the server, create an instance of the HttpServer class and call the start() function.
2. To set custom logics for any of the http request methods, call the setRequestProcessor() method and pass an instance of the RequestProcessor class.
3. If no RequestProcessor is set, the server returns the default http response.

The jar file can also be used instead. The jar file contains all the compiled classes. 