package io.github.parthappm.http.server;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Request object will contain all the details of the HTTP request.
 */
public class Request
{
	private final String ip;
	private String method;
	private String path;
	private String httpVersion;
	private final Map<String, String> pathParameters;
	private final Map<String, String> parameters;
	private final Map<String, String> headers;
	private byte[] body;

	Request(String ip)
	{
		this.ip = ip;
		this.method = "";
		this.path = "/";
		this.httpVersion = "";
		this.pathParameters = new HashMap<>();
		this.parameters = new HashMap<>();
		this.headers = new HashMap<>();
		this.body = new byte[0];
	}

	/**
	 * Returns the IP Address of the client making the request.
	 * @return IP Address.
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * Returns the HTTP request method: GET, POST, etc.
	 * @return HTTP request method.
	 */
	public String getMethod()
	{
		return method;
	}

	void setMethod(String method)
	{
		this.method = method.toUpperCase();
	}

	/**
	 * Returns the path or context of the HTTP request. The part after the host in the url. If no path is present in the url, then "/" is returned.
	 * @return HTTP request path or context.
	 */
	public String getPath()
	{
		return path;
	}

	void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Returns the HTTP request version. E.g.: HTTP/1.1
	 * @return The HTTP request version.
	 */
	public String getHttpVersion()
	{
		return httpVersion;
	}

	void setHttpVersion(String httpVersion)
	{
		this.httpVersion = httpVersion;
	}

	/**
	 * Returns Map object containing all the path parameters present in the URL as configured in the controller.
	 * @return Map of String to String.
	 */
	public Map<String, String> getPathParameters()
	{
		return pathParameters;
	}

	void addPathParameters(String key, String value)
	{
		this.pathParameters.put(key, value);
	}

	/**
	 * Returns Map object containing all the query parameters present in the URL after the path or context.
	 * @return Map of String to String.
	 */
	public Map<String, String> getParameters()
	{
		return parameters;
	}

	void setParameters(String parametersString)
	{
		if (parametersString != null && !parametersString.isEmpty())
		{
			for (String parameterItem : parametersString.split("&"))
			{
				String[] keyValue = parameterItem.split("=");
				if (keyValue.length == 1)
				{
					parameters.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8), "");
				}
				else if (keyValue.length == 2)
				{
					parameters.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8), URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
				}
			}
		}
	}

	/**
	 * Returns Map of object containing all the HTTP request headers.
	 * @return Map of String to String.
	 */
	public Map<String, String> getHeaders()
	{
		return headers;
	}

	void addHeader(String key, String value)
	{
		this.headers.put(key, value);
	}

	/**
	 * Returns HTTP request body. If request body is not present then this method returns an array of length zero.
	 * @return Bytes array.
	 */
	public byte[] getBody()
	{
		return body;
	}

	void setBody(byte[] body)
	{
		this.body = body;
	}
}