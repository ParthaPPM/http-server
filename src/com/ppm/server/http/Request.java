package com.ppm.server.http;

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
		this.parameters = parameters;
		this.headers = headers;
		this.body = body;
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

	public String toString()
	{
		String lineSeparator = System.lineSeparator();
		StringJoiner sj = new StringJoiner("&");
		Set<String> keySet = parameters.keySet();
		for (String key : keySet)
		{
			sj.add(key + "=" + parameters.get(key));
		}

		StringBuilder sb = new StringBuilder();
		sb.append(method).append(" ").append(url);
		if (parameters.size()!=0)
		{
			sb.append("?");
			try
			{
				sb.append(URLDecoder.decode(sj.toString(), StandardCharsets.UTF_8.toString()));
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		sb.append(" ").append(version).append(lineSeparator);
		keySet = headers.keySet();
		for (String key : keySet)
		{
			sb.append(key).append(": ").append(headers.get(key)).append(lineSeparator);
		}
		return sb.toString();
	}
}