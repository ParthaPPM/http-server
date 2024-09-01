package io.github.parthappm.http.server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An instance of this class is to be returned from the process method of Controller interface.
 * An object of this class contains all the details that are to be returned to the client for a single HTTP request.
 */
public class Response
{
	private record TemplateReplaceData(String statusText, String message, String emoji)
	{}

	private final int statusCode;
	private final String statusText;
	private final Map<String, String> headers;
	private byte[] body;
	private static final String responseBodyTemplate;
	private static final Map<Integer, TemplateReplaceData> templateReplaceDataMap;

	static
	{
		responseBodyTemplate = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"><title>${code} ${title}</title><style>body{font-family:\"Segoe UI\",Tahoma,Geneva,Verdana,sans-serif;text-align:center;background-color:#e9ecef;margin:0;padding:0;color:#495057}.container{display:flex;flex-direction:column;align-items:center;justify-content:center;min-height:100vh;text-align:center}.emoji{font-size:100px;margin-bottom:20px}.code{font-size:120px;margin:0;color:#dc3545}.message{font-size:18px;margin:20px 0;color:#343a40}</style></head><body><div class=\"container\"><div class=\"emoji\">${emoji}</div><h1 class=\"code\">${code}</h1><h2 class=\"title\">${title}</h2><p class=\"message\">${message}</p></div></body></html>";
		templateReplaceDataMap = new HashMap<>();
		templateReplaceDataMap.put(100, new TemplateReplaceData("Continue", "The server has received the request headers, and the client should proceed to send the request body.", "⏩"));
		templateReplaceDataMap.put(101, new TemplateReplaceData("Switching Protocol", "The server is switching protocols as requested by the client.", "🔄"));
		templateReplaceDataMap.put(102, new TemplateReplaceData("Processing", null, null));
		templateReplaceDataMap.put(103, new TemplateReplaceData("Early Hints", null, null));
		templateReplaceDataMap.put(200, new TemplateReplaceData("OK", "The request has succeeded. Your server is running fine.", "✅"));
		templateReplaceDataMap.put(201, new TemplateReplaceData("Created", "The request has succeeded, and a new resource has been created.", "🎉"));
		templateReplaceDataMap.put(202, new TemplateReplaceData("Accepted", "The request has been accepted for processing, but the processing has not been completed.", "⏳"));
		templateReplaceDataMap.put(203, new TemplateReplaceData("Non-Authoritative Information", "The request was successful but the information returned may be from a local or third-party copy.", "📝"));
		templateReplaceDataMap.put(204, new TemplateReplaceData("No Content", null, null));
		templateReplaceDataMap.put(205, new TemplateReplaceData("Reset Content", "The request was successful, and the client should reset the document view.", "🔄"));
		templateReplaceDataMap.put(206, new TemplateReplaceData("Partial Content", "The server is delivering only part of the resource due to a range header sent by the client.", "📄"));
		templateReplaceDataMap.put(207, new TemplateReplaceData("Multi-Status", "Multiple operations completed. Check individual results for details", "📋"));
		templateReplaceDataMap.put(208, new TemplateReplaceData("Already Reported", "This information has already been reported.", "📄"));
		templateReplaceDataMap.put(226, new TemplateReplaceData("IM Used", "Request fulfilled; modifications applied.", "🔧"));
		templateReplaceDataMap.put(300, new TemplateReplaceData("Multiple Choice", "The request has more than one possible response.", "🔀"));
		templateReplaceDataMap.put(301, new TemplateReplaceData("Moved Permanently", "The resource has been moved to a new permanent URI.", "➡️"));
		templateReplaceDataMap.put(302, new TemplateReplaceData("Found", "The resource is temporarily located at a different URI.", "📍"));
		templateReplaceDataMap.put(303, new TemplateReplaceData("See Other", "The response to the request can be found under a different URI using a GET method.", "👀"));
		templateReplaceDataMap.put(304, new TemplateReplaceData("Not Modified", "The resource has not been modified since the last request.", "🗂️"));
		templateReplaceDataMap.put(305, new TemplateReplaceData("Use Proxy", "The requested resource must be accessed through the proxy given by the server.", "🔌"));
		templateReplaceDataMap.put(306, new TemplateReplaceData("Switch Proxy", "Switch to a different proxy server for this request.", "🔀"));
		templateReplaceDataMap.put(307, new TemplateReplaceData("Temporary Redirect", "The request should be repeated with another URI, but future requests should use the original URI.", "🚧"));
		templateReplaceDataMap.put(308, new TemplateReplaceData("Permanent Redirect", "The resource has been permanently moved to a new URI, and future requests should use the new URI.", "🏠"));
		templateReplaceDataMap.put(400, new TemplateReplaceData("Bad Request", "Oops! The server could not understand your request. Please check your input and try again.", "🚫"));
		templateReplaceDataMap.put(401, new TemplateReplaceData("Unauthorized", "Sorry, you need to log in to access this page. Please check your credentials and try again.", "🔐"));
		templateReplaceDataMap.put(402, new TemplateReplaceData("Payment Required", "Payment is required to access this resource. Please ensure you have the necessary payment details.", "💳"));
		templateReplaceDataMap.put(403, new TemplateReplaceData("Forbidden", "Sorry, you don’t have permission to access this page. If you believe this is an error, contact support.", "🚷"));
		templateReplaceDataMap.put(404, new TemplateReplaceData("Not Found", "Sorry, the page you’re looking for doesn’t exist.", "🚫"));
		templateReplaceDataMap.put(405, new TemplateReplaceData("Method Not Allowed", "Sorry, the HTTP method used is not allowed for this resource. Please check your request and try again.", "🚫"));
		templateReplaceDataMap.put(406, new TemplateReplaceData("Not Acceptable", "The resource is capable of generating only content not acceptable according to the Accept headers.", "📜"));
		templateReplaceDataMap.put(407, new TemplateReplaceData("Proxy Authentication Required", "The client must first authenticate itself with the proxy.", "🔑"));
		templateReplaceDataMap.put(408, new TemplateReplaceData("Request Timeout", "The server timed out waiting for the request. Please try again later.", "⏳"));
		templateReplaceDataMap.put(409, new TemplateReplaceData("Conflict", "The request could not be completed due to a conflict with the current state of the resource.", "⚠️"));
		templateReplaceDataMap.put(410, new TemplateReplaceData("Gone", "The resource requested is no longer available and will not be available again.", "🕳️"));
		templateReplaceDataMap.put(411, new TemplateReplaceData("Length Required", "The request did not specify the length of its content, which is required by the server.", "📏"));
		templateReplaceDataMap.put(412, new TemplateReplaceData("Precondition Failed", "The server does not meet one of the preconditions that the requester put on the request.", "⚠️"));
		templateReplaceDataMap.put(413, new TemplateReplaceData("Payload Too Large", "The request is larger than the server is willing or able to process.", "📦"));
		templateReplaceDataMap.put(414, new TemplateReplaceData("URI Too Long", "The URI provided was too long for the server to process.", "🔗"));
		templateReplaceDataMap.put(415, new TemplateReplaceData("Unsupported Media Type", "The request entity has a media type which the server or resource does not support.", "🗃️"));
		templateReplaceDataMap.put(416, new TemplateReplaceData("Range Not Satisfiable", "The range specified by the Range header field in the request cannot be fulfilled.", "📉"));
		templateReplaceDataMap.put(417, new TemplateReplaceData("Expectation Failed", "The server cannot meet the requirements of the Expect request-header field.", "🤔"));
		templateReplaceDataMap.put(418, new TemplateReplaceData("I'm a teapot", "Any attempt to instruct a teapot to brew coffee should result in the error code \"418 I'm a teapot\".", "☕"));
		templateReplaceDataMap.put(421, new TemplateReplaceData("Misdirected Request", "The request was directed at a server that is not able to produce a response.", "🔄"));
		templateReplaceDataMap.put(422, new TemplateReplaceData("Unprocessable Entity", "The request was well-formed but was unable to be followed due to semantic errors.", "🛠️"));
		templateReplaceDataMap.put(423, new TemplateReplaceData("Locked", "The resource that is being accessed is locked.", "🔒"));
		templateReplaceDataMap.put(424, new TemplateReplaceData("Failed Dependency", "The request failed due to failure of a previous request.", "🔗"));
		templateReplaceDataMap.put(425, new TemplateReplaceData("Too Early", "The server is unwilling to risk processing a request that might be replayed.", "⏰"));
		templateReplaceDataMap.put(426, new TemplateReplaceData("Upgrade Required", "The server refuses to perform the request using the current protocol but might be willing to do so after the client upgrades to a different protocol.", "⚙️"));
		templateReplaceDataMap.put(428, new TemplateReplaceData("Precondition Required", "The origin server requires the request to be conditional.", "⚠️"));
		templateReplaceDataMap.put(429, new TemplateReplaceData("Too Many Requests", "The user has sent too many requests in a given amount of time.", "⛔"));
		templateReplaceDataMap.put(431, new TemplateReplaceData("Request Header Fields Too Large", "The server is unwilling to process the request because its header fields are too large.", "📋"));
		templateReplaceDataMap.put(451, new TemplateReplaceData("Unavailable For Legal Reasons", "The resource is unavailable due to legal reasons.", "⚖️"));
		templateReplaceDataMap.put(500, new TemplateReplaceData("Internal Server Error", "The server encountered an unexpected condition that prevented it from fulfilling the request.", "💣"));
		templateReplaceDataMap.put(501, new TemplateReplaceData("Not Implemented", "The server does not support the functionality required to fulfill the request.", "🔨"));
		templateReplaceDataMap.put(502, new TemplateReplaceData("Bad Gateway", "The server, while acting as a gateway or proxy, received an invalid response from the upstream server.", "🌉"));
		templateReplaceDataMap.put(503, new TemplateReplaceData("Service Unavailable", "The server is currently unable to handle the request due to temporary overloading or maintenance.", "🚧"));
		templateReplaceDataMap.put(504, new TemplateReplaceData("Gateway Timeout", "The server, while acting as a gateway or proxy, did not receive a timely response from the upstream server.", "⏱️"));
		templateReplaceDataMap.put(505, new TemplateReplaceData("HTTP Version Not Supported", "The server does not support the HTTP protocol version that was used in the request.", "🔧"));
		templateReplaceDataMap.put(506, new TemplateReplaceData("Variant Also Negotiates", "The server has an internal configuration error: transparent content negotiation for the request results in a circular reference.", "🔄"));
		templateReplaceDataMap.put(507, new TemplateReplaceData("Insufficient Storage", "The server is unable to store the representation needed to complete the request.", "📦"));
		templateReplaceDataMap.put(508, new TemplateReplaceData("Loop Detected", "The server detected an infinite loop while processing a request with \"Depth: infinity\".", "🔁"));
		templateReplaceDataMap.put(510, new TemplateReplaceData("Not Extended", "Further extensions to the request are required for the server to fulfill it.", "🔧"));
		templateReplaceDataMap.put(511, new TemplateReplaceData("Network Authentication Required", "Network access requires authentication. Please log in.", "🔐"));
	}

	/**
	 * Creates a Response object with 200 status code and default response body.
	 */
	public Response()
	{
		this(200);
	}

	/**
	 * Creates a Response object with specified status code and default response body.
	 * @param statusCode The HTTP status code.
	 */
	public Response(int statusCode)
	{
		TemplateReplaceData templateReplaceData = templateReplaceDataMap.getOrDefault(statusCode, new TemplateReplaceData("Unknown code", null, null));
		this.statusCode = statusCode;
		this.statusText = templateReplaceData.statusText;
		this.headers = new HashMap<>();

		if (templateReplaceData.message != null)
		{
			addHeader("Content-Type", "text/html");
			this.body = responseBodyTemplate
					.replace("${code}", String.valueOf(statusCode))
					.replace("${title}", statusText)
					.replace("${message}", templateReplaceData.message)
					.replace("${emoji}", templateReplaceData.emoji)
					.getBytes(StandardCharsets.UTF_8);
		}
		else
		{
			this.body = new byte[0];
		}
	}

	/**
	 * Setter method to add or modify a single HTTP header.
	 * If a header is to be deleted, then call this method with the header name (key) and value as null.
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
	 * @param responseObject The HTTP response body. Can be Bytes Array, String or any Java Object.
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