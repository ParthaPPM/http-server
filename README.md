# Nebula (HTTP Server)
This is a http server program which can be used with another program that requires a (simple) server implementation.

**Feel free to give any feedback.**

[![Project License](https://img.shields.io/github/license/ParthaPPM/http-server)](https://github.com/ParthaPPM/http-server/blob/master/LICENSE)
[![javadoc](https://javadoc.io/badge2/io.github.parthappm/http-server/javadoc.svg)](https://javadoc.io/doc/io.github.parthappm/http-server)
[![POM](https://img.shields.io/maven-central/v/io.github.parthappm/http-server)](https://central.sonatype.com/artifact/io.github.parthappm/http-server/2.0.1)
[![Artifact JAR](https://javadoc.io/badge2/io.github.parthappm/http-server/JAR.svg)](https://repo1.maven.org/maven2/io/github/parthappm/http-server/2.0.1/http-server-2.0.1.jar)

## How to use:-

[Main.java](https://github.com/ParthaPPM/lib-java-http-server/blob/main/lib/src/test/java/Main.java) file can be referred for example.

**Step 1 (Optional):**
Create an object of `ServerProperties` class. All the properties are optional. The KeyStoreFileName and KeyStorePassword are required for HTTPS server.
```java
ServerProperties serverProperties = new ServerProperties();
serverProperties.setPort(8080);
serverProperties.setHost("127.0.0.1");
serverProperties.setKeyStoreFileName("/path/to/file.jks");
serverProperties.setKeyStorePassword("password");
serverProperties.setServerTimeoutInMillis(10000);
serverProperties.setServerName("Test Server");
serverProperties.setRootDirectory("");
serverProperties.setLogFileName("log_file.txt");
serverProperties.setLogDateFormat("[HH:mm:ss]");
```

**Step 2:**
To start the server, create an instance of the `HttpServer` or `HttpsServer` class and assign it to Server class. Optionally, the `serverProperties` can be passed.
```
Server httpServer = new HttpServer();
Server httpServer = new HttpServer(serverProperties);
Server httpsServer = new HttpsServer();
Server httpsServer = new HttpsServer(serverProperties);
```

**Step 3:**
Add the controllers to the server.
```java
// create response object with 404 not found error and default response body
httpServer.addController("GET", "/index.html", request -> new Response(404));

// create response object with 200 status code and "This is index page" as the response body
httpServer.addController("GET", "/index.html", request -> {
    // process the request
    return new Response().setBody("This is index page");
});
```

**Step 4:**
Call the `start()` method to start the server.
```java
httpServer.start();
```

**Step 5:**
Call the `stop()` method to stop the server.
```java
httpServer.stop();
```