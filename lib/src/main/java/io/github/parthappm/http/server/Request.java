package io.github.parthappm.http.server;

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
		this.method = method.toUpperCase();
		String s = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
		this.url = s.startsWith("/") ? s : ("/" + s);
		this.version = version;
		this.parameters = parameters;
		this.headers = headers;
		this.body = body;
	}

	String method()
	{
		return method;
	}

	String url()
	{
		return url;
	}

	String version()
	{
		return version;
	}

	Map<String, String> parameters()
	{
		return parameters;
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