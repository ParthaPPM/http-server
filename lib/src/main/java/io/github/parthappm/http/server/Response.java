package io.github.parthappm.http.server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An instance of this class is to be created and returned from the methods of the RequestProcessor class.
 */
public class Response
{
	private record ResponseMetadata(String statusText, String message, String emoji)
	{}

	private final int statusCode;
	private final String statusText;
	private final Map<String, String> headers;
	private byte[] body;
	private static final String responseBodyTemplate;
	private static final Map<Integer, ResponseMetadata> responseMetadataMap;

	static
	{
		responseBodyTemplate = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"><title>${code} ${title}</title><style>body{font-family:\"Segoe UI\",Tahoma,Geneva,Verdana,sans-serif;text-align:center;background-color:#e9ecef;margin:0;padding:0;color:#495057}.container{display:flex;flex-direction:column;align-items:center;justify-content:center;min-height:100vh;text-align:center}.emoji{font-size:100px;margin-bottom:20px}.code{font-size:120px;margin:0;color:#dc3545}.message{font-size:18px;margin:20px 0;color:#343a40}</style></head><body><div class=\"container\"><div class=\"emoji\">${emoji}</div><h1 class=\"code\">${code}</h1><h2 class=\"title\">${title}</h2><p class=\"message\">${message}</p></div></body></html>";
		responseMetadataMap = new HashMap<>();
		responseMetadataMap.put(100, new ResponseMetadata("Continue", "The server has received the request headers, and the client should proceed to send the request body.", "⏩"));
		responseMetadataMap.put(101, new ResponseMetadata("Switching Protocol", "The server is switching protocols as requested by the client.", "🔄"));
		responseMetadataMap.put(102, new ResponseMetadata("Processing", null, null));
		responseMetadataMap.put(103, new ResponseMetadata("Early Hints", null, null));
		responseMetadataMap.put(200, new ResponseMetadata("OK", "The request has succeeded. Your server is running fine.", "✅"));
		responseMetadataMap.put(201, new ResponseMetadata("Created", "The request has succeeded, and a new resource has been created.", "🎉"));
		responseMetadataMap.put(202, new ResponseMetadata("Accepted", "The request has been accepted for processing, but the processing has not been completed.", "⏳"));
		responseMetadataMap.put(203, new ResponseMetadata("Non-Authoritative Information", "The request was successful but the information returned may be from a local or third-party copy.", "📝"));
		responseMetadataMap.put(204, new ResponseMetadata("No Content", null, null));
		responseMetadataMap.put(205, new ResponseMetadata("Reset Content", "The request was successful, and the client should reset the document view.", "🔄"));
		responseMetadataMap.put(206, new ResponseMetadata("Partial Content", "The server is delivering only part of the resource due to a range header sent by the client.", "📄"));
		responseMetadataMap.put(207, new ResponseMetadata("Multi-Status", "Multiple operations completed. Check individual results for details", "📋"));
		responseMetadataMap.put(208, new ResponseMetadata("Already Reported", "This information has already been reported.", "📄"));
		responseMetadataMap.put(226, new ResponseMetadata("IM Used", "Request fulfilled; modifications applied.", "🔧"));
		responseMetadataMap.put(300, new ResponseMetadata("Multiple Choice", "The request has more than one possible response.", "🔀"));
		responseMetadataMap.put(301, new ResponseMetadata("Moved Permanently", "The resource has been moved to a new permanent URI.", "➡️"));
		responseMetadataMap.put(302, new ResponseMetadata("Found", "The resource is temporarily located at a different URI.", "📍"));
		responseMetadataMap.put(303, new ResponseMetadata("See Other", "The response to the request can be found under a different URI using a GET method.", "👀"));
		responseMetadataMap.put(304, new ResponseMetadata("Not Modified", "The resource has not been modified since the last request.", "🗂️"));
		responseMetadataMap.put(305, new ResponseMetadata("Use Proxy", "The requested resource must be accessed through the proxy given by the server.", "🔌"));
		responseMetadataMap.put(306, new ResponseMetadata("Switch Proxy", "Switch to a different proxy server for this request.", "🔀"));
		responseMetadataMap.put(307, new ResponseMetadata("Temporary Redirect", "The request should be repeated with another URI, but future requests should use the original URI.", "🚧"));
		responseMetadataMap.put(308, new ResponseMetadata("Permanent Redirect", "The resource has been permanently moved to a new URI, and future requests should use the new URI.", "🏠"));
		responseMetadataMap.put(400, new ResponseMetadata("Bad Request", "Oops! The server could not understand your request. Please check your input and try again.", "🚫"));
		responseMetadataMap.put(401, new ResponseMetadata("Unauthorized", "Sorry, you need to log in to access this page. Please check your credentials and try again.", "🔐"));
		responseMetadataMap.put(402, new ResponseMetadata("Payment Required", "Payment is required to access this resource. Please ensure you have the necessary payment details.", "💳"));
		responseMetadataMap.put(403, new ResponseMetadata("Forbidden", "Sorry, you don’t have permission to access this page. If you believe this is an error, contact support.", "🚷"));
		responseMetadataMap.put(404, new ResponseMetadata("Not Found", "Sorry, the page you’re looking for doesn’t exist.", "🚫"));
		responseMetadataMap.put(405, new ResponseMetadata("Method Not Allowed", "Sorry, the HTTP method used is not allowed for this resource. Please check your request and try again.", "🚫"));
		responseMetadataMap.put(406, new ResponseMetadata("Not Acceptable", "The resource is capable of generating only content not acceptable according to the Accept headers.", "📜"));
		responseMetadataMap.put(407, new ResponseMetadata("Proxy Authentication Required", "The client must first authenticate itself with the proxy.", "🔑"));
		responseMetadataMap.put(408, new ResponseMetadata("Request Timeout", "The server timed out waiting for the request. Please try again later.", "⏳"));
		responseMetadataMap.put(409, new ResponseMetadata("Conflict", "The request could not be completed due to a conflict with the current state of the resource.", "⚠️"));
		responseMetadataMap.put(410, new ResponseMetadata("Gone", "The resource requested is no longer available and will not be available again.", "🕳️"));
		responseMetadataMap.put(411, new ResponseMetadata("Length Required", "The request did not specify the length of its content, which is required by the server.", "📏"));
		responseMetadataMap.put(412, new ResponseMetadata("Precondition Failed", "The server does not meet one of the preconditions that the requester put on the request.", "⚠️"));
		responseMetadataMap.put(413, new ResponseMetadata("Payload Too Large", "The request is larger than the server is willing or able to process.", "📦"));
		responseMetadataMap.put(414, new ResponseMetadata("URI Too Long", "The URI provided was too long for the server to process.", "🔗"));
		responseMetadataMap.put(415, new ResponseMetadata("Unsupported Media Type", "The request entity has a media type which the server or resource does not support.", "🗃️"));
		responseMetadataMap.put(416, new ResponseMetadata("Range Not Satisfiable", "The range specified by the Range header field in the request cannot be fulfilled.", "📉"));
		responseMetadataMap.put(417, new ResponseMetadata("Expectation Failed", "The server cannot meet the requirements of the Expect request-header field.", "🤔"));
		responseMetadataMap.put(418, new ResponseMetadata("I'm a teapot", "Any attempt to instruct a teapot to brew coffee should result in the error code \"418 I'm a teapot\".", "☕"));
		responseMetadataMap.put(421, new ResponseMetadata("Misdirected Request", "The request was directed at a server that is not able to produce a response.", "🔄"));
		responseMetadataMap.put(422, new ResponseMetadata("Unprocessable Entity", "The request was well-formed but was unable to be followed due to semantic errors.", "🛠️"));
		responseMetadataMap.put(423, new ResponseMetadata("Locked", "The resource that is being accessed is locked.", "🔒"));
		responseMetadataMap.put(424, new ResponseMetadata("Failed Dependency", "The request failed due to failure of a previous request.", "🔗"));
		responseMetadataMap.put(425, new ResponseMetadata("Too Early", "The server is unwilling to risk processing a request that might be replayed.", "⏰"));
		responseMetadataMap.put(426, new ResponseMetadata("Upgrade Required", "The server refuses to perform the request using the current protocol but might be willing to do so after the client upgrades to a different protocol.", "⚙️"));
		responseMetadataMap.put(428, new ResponseMetadata("Precondition Required", "The origin server requires the request to be conditional.", "⚠️"));
		responseMetadataMap.put(429, new ResponseMetadata("Too Many Requests", "The user has sent too many requests in a given amount of time.", "⛔"));
		responseMetadataMap.put(431, new ResponseMetadata("Request Header Fields Too Large", "The server is unwilling to process the request because its header fields are too large.", "📋"));
		responseMetadataMap.put(451, new ResponseMetadata("Unavailable For Legal Reasons", "The resource is unavailable due to legal reasons.", "⚖️"));
		responseMetadataMap.put(500, new ResponseMetadata("Internal Server Error", "The server encountered an unexpected condition that prevented it from fulfilling the request.", "💣"));
		responseMetadataMap.put(501, new ResponseMetadata("Not Implemented", "The server does not support the functionality required to fulfill the request.", "🔨"));
		responseMetadataMap.put(502, new ResponseMetadata("Bad Gateway", "The server, while acting as a gateway or proxy, received an invalid response from the upstream server.", "🌉"));
		responseMetadataMap.put(503, new ResponseMetadata("Service Unavailable", "The server is currently unable to handle the request due to temporary overloading or maintenance.", "🚧"));
		responseMetadataMap.put(504, new ResponseMetadata("Gateway Timeout", "The server, while acting as a gateway or proxy, did not receive a timely response from the upstream server.", "⏱️"));
		responseMetadataMap.put(505, new ResponseMetadata("HTTP Version Not Supported", "The server does not support the HTTP protocol version that was used in the request.", "🔧"));
		responseMetadataMap.put(506, new ResponseMetadata("Variant Also Negotiates", "The server has an internal configuration error: transparent content negotiation for the request results in a circular reference.", "🔄"));
		responseMetadataMap.put(507, new ResponseMetadata("Insufficient Storage", "The server is unable to store the representation needed to complete the request.", "📦"));
		responseMetadataMap.put(508, new ResponseMetadata("Loop Detected", "The server detected an infinite loop while processing a request with \"Depth: infinity\".", "🔁"));
		responseMetadataMap.put(510, new ResponseMetadata("Not Extended", "Further extensions to the request are required for the server to fulfill it.", "🔧"));
		responseMetadataMap.put(511, new ResponseMetadata("Network Authentication Required", "Network access requires authentication. Please log in.", "🔐"));
	}

	public Response()
	{
		this(200);
	}

	/**
	 * Creates a Response object with specified status code and empty body.
	 * @param statusCode The HTTP status code.
	 */
	public Response(int statusCode)
	{
		ResponseMetadata responseMetadata = responseMetadataMap.getOrDefault(statusCode, new ResponseMetadata("Unknown code", null, null));
		this.statusCode = statusCode;
		this.statusText = responseMetadata.statusText;
		this.headers = new HashMap<>();

		if (responseMetadata.message != null)
		{
			addHeader("Content-Type", "text/html");
			this.body = responseBodyTemplate
					.replace("${code}", String.valueOf(statusCode))
					.replace("${title}", statusText)
					.replace("${message}", responseMetadata.message)
					.replace("${emoji}", responseMetadata.emoji)
					.getBytes(StandardCharsets.UTF_8);
		}
		else
		{
			this.body = new byte[0];
		}
	}

	/**
	 * Setter method to add or modify a single HTTP header.
	 * @param key The header name.
	 * @param value The header value.
	 * @return The reference of the current object for chaining.
	 */
	public Response addHeader(String key, String value)
	{
		if (key != null &&
				!key.isEmpty() &&
				!key.equalsIgnoreCase("Date") &&
				!key.equalsIgnoreCase("Server") &&
				!key.equalsIgnoreCase("Content-Length"))
		{
			if (value == null)
			{
				headers.remove(key);
			}
			else
			{
				headers.put(key, value);
			}
		}
		return this;
	}

	/**
	 * Setter method to set the HTTP response body.
	 * @param responseObject The HTTP response body in String.
	 * @return The reference of the current object for chaining.
	 */
	public Response setBody(Object responseObject)
	{
		if (responseObject instanceof byte[])
		{
			this.body = (byte[]) responseObject;
		}
		else if (responseObject instanceof String)
		{
			this.body = ((String) responseObject).getBytes(StandardCharsets.UTF_8);
		}
		else if (responseObject instanceof List<?>)
		{
			//TODO convert list to json string
			this.body = responseObject.toString().getBytes(StandardCharsets.UTF_8);
		}
		else if (responseObject instanceof Map<?,?>)
		{
			//TODO convert map to json string
			this.body = responseObject.toString().getBytes(StandardCharsets.UTF_8);
		}
		else
		{
			//TODO convert java object to json string using reflection
			this.body = responseObject.toString().getBytes(StandardCharsets.UTF_8);
		}

		return this;
	}

	int statusCode()
	{
		return statusCode;
	}

	String statusText()
	{
		return statusText;
	}

	Map<String, String> headers()
	{
		return headers;
	}

	byte[] body()
	{
		return body;
	}
}