# Slow-Server
This is my http server program which can be used with another program which requires a (simple) server implementation.

**How to use:-**

1. To start the server, create an instance of the HttpServer or HttpsServer class and call the start() function.
2. To set the logics for any of the http request methods, call the setRequestProcessor() method and pass an instance of the RequestProcessor class.
3. For HttpsServer only:- set **keyStoreFileName** as the filename (with the path) for the keyStoreFile, set **password** as the password for the keyStore file.

**Note:-**
1. If no RequestProcessor is set, the server returns the default http response.
2. To use Https Server you must have the **server.config** file in the base location. For HttpServer, this file is not required. 

The jar file can also be used instead. The jar file contains all the compiled classes.

### Example:-
class TestRequestProcessor extends RequestProcessor\
{...}\
TestRequestProcessor testRequestProcessor = new TestRequestProcessor();

**Start an HTTP Server**\
Server server = new HttpServer()\
server.setRequestProcessor(testRequestProcessor);\
server.start();

**Start an HTTPS Server**\
Server server = new HttpsServer()\
server.setRequestProcessor(testRequestProcessor);\
server.start();

## Documentation:-

### Server (An interface for HttpServer and HttpsServer objects)
|Name|Return Type|Description|
|---|---|---|
|**setRequestProcessor (RequestProcessor requestProcessor)**|void|Sets the requestProcessor object|
|**start ()**|void|Starts the server|
|**stop ()**|void|Stops the server|
|**isServerRunning ()**|boolean|Returns whether the server is running or not|

### HttpServer implements Server
|Name|Return Type|Description|
|---|---|---|
|**HttpServer ()**| |Initializes an instance of this class|
|**HttpServer (int port)**| |**port** - specifies the port number for the server|
|**HttpServer (boolean showLog)**| |**showLog** - specifies whether to show log to the console|
|**HttpServer (int port, boolean showLog)**| |**port** - specifies the port number for the server <br/> **showLog** - specifies whether to show log to the console|

### HttpsServer implements Server
|Name|Return Type|Description|
|---|---|---|
|**HttpsServer ()**| |Initializes an instance of this class|
|**HttpsServer (int port)**| |**port** - specifies the port number for the server|
|**HttpsServer (boolean showLog)**| |**showLog** - specifies whether to show log to the console|
|**HttpsServer (int port, boolean showLog)**| |**port** - specifies the port number for the server <br/> **showLog** - specifies whether to show log to the console|

### PartialResponse
|Name|Return Type|Description|
|---|---|---|
|**PartialResponse ()**| |Initializes an instance of this class|
|**PartialResponse (byte[] body)**| |**body** - the response body in byte array|
|**PartialResponse (Map<String, String> customHeaders, byte[] body)**| |**customHeaders** - the extra headers that are to be added to the response <br/> **body** - the response body in byte array|
|**PartialResponse (int responseCode, Map<String, String> customHeaders)**| |**responseCode** - the status code of the response <br/> **customHeaders** - the extra headers that are to be added to the response|
|**PartialResponse (int responseCode, byte[] body)**| |**responseCode** - the status code of the response <br/> **body** - the response body in byte array|
|**PartialResponse (int responseCode, Map<String, String> customHeaders, byte[] body)**| |**responseCode** - the status code of the response <br/> **customHeaders** - the extra headers that are to be added to the response <br/> **body** - the response body in byte array|
|**getResponseCode ()**|int|Returns the status code of the response|
|**getCustomHeaders ()**|Map<String, String>|Returns the headers of the response|
|**getBody ()**|byte[]|Returns the response body in byte array|

### RequestProcessor
|Name|Return Type|Description|
|---|---|---|
|**RequestProcessor ()**| |Initializes an instance of this class|
|**get (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the get request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**head (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the head request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**post (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the post request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**put (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the put request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**delete (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the delete request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**connect (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the connect request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**options (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the options request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**trace (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the trace request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**patch (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the patch request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array|
|**none (String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)**|PartialResponse|This function handles all the none request of the server <br/> **url** - the url of the request (excluding the host name) <br/> **requestParameters** - the request parameters as key value Map object <br/> **requestHeaders** - the request headers as key value Map object <br/> **requestBody** - the request body as byte array <br/> This function is called if the http method is not recognised by the server.|