package com.ppm.http.server;

import java.util.Map;

class Request
{
	private final String method;
	private final String url;
	private final String version;
	private final Map<String, String> parameters;
	private final Map<String, String> headers;
	private final byte[] body;

	Request(String method, String url, String version, Map<String, String> parameters, Map<String, String> headers, byte[] body)
	{
		this.method = method;
		this.url = url;
		this.version = version;
		this.parameters = parameters;
		this.headers = headers;
		if(body != null && body.length == 0)
		{
			this.body = null;
		}
		else
		{
			this.body = body;
		}
	}

	String getMethod()
	{
		return method;
	}

	String getUrl()
	{
		return url;
	}

	String getVersion()
	{
		return version;
	}

	Map<String, String> getParameters()
	{
		return parameters;
	}

	Map<String, String> getHeaders()
	{
		return headers;
	}

	byte[] getBody()
	{
		return body;
	}
}