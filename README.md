# Slow-Server

This is a http server program which can be used with another program that requires a (simple) server implementation.

**Feel free to give any feedback.**

[![Project License](https://img.shields.io/github/license/ParthaPPM/http-server)](https://github.com/ParthaPPM/http-server/blob/master/LICENSE)
[![javadoc](https://javadoc.io/badge2/io.github.parthappm/http-server/javadoc.svg)](https://javadoc.io/doc/io.github.parthappm/http-server)
[![POM](https://img.shields.io/maven-central/v/io.github.parthappm/http-server)](https://central.sonatype.dev/artifact/io.github.parthappm/http-server/1.0.1)
[![Artifact JAR](https://javadoc.io/badge2/io.github.parthappm/http-server/JAR.svg)](https://repo1.maven.org/maven2/io/github/parthappm/http-server/1.0.1/http-server-1.0.1.jar)

## How to use:-

1. To start the server, create an instance of the `HttpServer` or `HttpsServer` class and assign it to Server class.
2. To set the logics for any of the http request methods, call the `setRequestProcessor()` method and pass an instance of the `RequestProcessor` class.
3. Call the `start()` function.
4. To set the request timeout, call the `setTimeout()` method and pass an instance of Duration class.
5. For HttpsServer only, the `setKeyStore()` method needs to be called with **keyStoreFileName** and **password**.

**Note:-**
1. If no RequestProcessor is set, the server returns the default http response.
2. If `setKeyStore()` method is not called then connection to HttpsServer cannot be established.

### Example:-
**Extending the RequestPreprocessor class**
```java
class TestRequestProcessor extends RequestProcessor
{
    // override the required methods
}
```
**Starting the server**
```ignorelang
TestRequestProcessor testRequestProcessor = new TestRequestProcessor();
Server server = new HttpServer(); // Starts an HTTP Server
Server server = new HttpsServer(); // Starts an HTTPS Server
server.setRequestProcessor(testRequestProcessor);
server.start();
```