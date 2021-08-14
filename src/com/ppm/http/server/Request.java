package com.ppm.http.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

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
		if(parameters != null && parameters.size() == 0)
		{
			this.parameters = null;
		}
		else
		{
			this.parameters = parameters;
		}
		if(headers != null && headers.size() == 0)
		{
			this.headers = null;
		}
		else
		{
			this.headers = headers;
		}
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