# Slow-Server
This is my http server program which can be used with another program that requires a (simple) server implementation.

**How to use:-**

1. To start the server, create an instance of the `HttpServer` or `HttpsServer` class and call the `start()` function.
2. To set the logics for any of the http request methods, call the `setRequestProcessor()` method and pass an instance of the `RequestProcessor` class.
3. To set the request timeout, call the `setTimeout()` method and pass an integer value denoting the time in milliseconds to wait before closing the connection. 
4. For HttpsServer only:- set **keyStoreFileName** as the filename (with the path) for the keyStoreFile, set **password** as the password for the keyStore file.

**Note:-**
1. If no RequestProcessor is set, the server returns the default http response.
2. To use Https Server you must have the **server.config** file in the base location. For HttpServer, this file is not required. 

The jar file can also be used instead. The jar file contains all the compiled classes.

### Example:-
**Extending the RequestPreprocessor class**
```java
class TestRequestProcessor extends RequestProcessor
{
    // override the required methods
}
```
**Main function for starting the server**
```java
TestRequestProcessor testRequestProcessor = new TestRequestProcessor();

Server server = new HttpServer(); // Starts an HTTP Server
Server server = new HttpsServer(); // Starts an HTTPS Server
server.setRequestProcessor(testRequestProcessor);
server.start();
```

## Documentation:-

### Server (An interface for HttpServer and HttpsServer objects)
| Name                                                        | Return Type | Description                                  |
|-------------------------------------------------------------|-------------|----------------------------------------------|
| **setRequestProcessor (RequestProcessor requestProcessor)** | void        | Sets the requestProcessor object             |
| **setTimeout (int milliSeconds)**                           | void        | Sets the request timeout                     |
| **start ()**                                                | void        | Starts the server                            |
| **stop ()**                                                 | void        | Stops the server                             |
| **isServerRunning ()**                                      | boolean     | Returns whether the server is running or not |

### HttpServer implements Server
| Name                                   | Return Type | Description                                                                                                          |
|----------------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------|
| **HttpServer ()**                      |             | Initializes an instance of this class                                                                                |
| **HttpServer (int port)**              |             | **port** - specifies the port number for the server                                                                  |
| **HttpServer (int port, String host)** |             | **port** - specifies the port number for the server <br/> **host** - specifies the host that the server will bind to |

### HttpsServer implements Server
| Name                                        | Return Type | Description                                                                                                          |
|---------------------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------|
| **HttpsServer ()**                          |             | Initializes an instance of this class                                                                                |
| **HttpsServer (int port)**                  |             | **port** - specifies the port number for the server                                                                  |
| **HttpsServer (int port, String host)**     |             | **port** - specifies the port number for the server <br/> **host** - specifies the host that the server will bind to |

### RequestProcessor
| Name                                                                                                                    | Return Type | Description                                                                                                                                                                                                                                                                                                                                                                                                            |
|-------------------------------------------------------------------------------------------------------------------------|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **RequestProcessor ()**                                                                                                 |             | Initializes an instance of this class                                                                                                                                                                                                                                                                                                                                                                                  |
| **get (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**     | Response    | This function handles all the get request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                    |
| **head (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**    | Response    | This function handles all the head request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                   |
| **post (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**    | Response    | This function handles all the post request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                   |
| **put (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**     | Response    | This function handles all the put request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                    |
| **delete (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**  | Response    | This function handles all the delete request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                 |
| **connect (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)** | Response    | This function handles all the connect request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                |
| **options (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)** | Response    | This function handles all the options request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                |
| **trace (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**   | Response    | This function handles all the trace request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                  |
| **patch (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**   | Response    | This function handles all the patch request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array                                                                                  |
| **none (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**    | Response    | This function handles all the none request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array <br/> This function is called if the http method is not recognised by the server. |

### Response
| Name                                                                     | Return Type | Description                                                                                                                                                                                                                             |
|--------------------------------------------------------------------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Response()**                                                           |             | Create a response object with status code 404                                                                                                                                                                                           |
| **Response(byte[] body)**                                                |             | Create a response object with status code 200 <br/> **body** - response body                                                                                                                                                            |
| **Response(int responseCode, byte[] body)**                              |             | Create a response object with custom status code <br/> **responseCode** - http response status code <br/> **body** - response body                                                                                                      |
| **Response(Map<String, String> headers, byte[] body)**                   |             | Create a response object with status code 200 <br/> **headers** - http extra headers that are to be added (headers may be overriden by the server) <br/> **body** - response body                                                       |
| **Response(int responseCode, Map<String, String> headers, byte[] body)** |             | Create a response object with custom status code <br/> **responseCode** - http response status code <br/> **headers** - http extra headers that are to be added (headers may be overriden by the server) <br/> **body** - response body |