package com.ppm.server.http;

import java.util.Map;
import java.util.Set;

class Response
{
	private final String version;
	private final int responseCode;
	private final String responseCodeText;
	private final Map<String, String> headers;
	private final byte[] body;

	Response(String version, int responseCode, String responseCodeText, Map<String, String> headers, byte[] body)
	{
		this.version = version;
		this.responseCode = responseCode;
		this.responseCodeText = responseCodeText;
		this.headers = headers;
		this.body = body;
	}

	String getVersion()
	{
		return version;
	}

	int getResponseCode()
	{
		return responseCode;
	}

	String getResponseCodeText()
	{
		return responseCodeText;
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
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = headers.keySet();
		sb.append(version).append(" ").append(responseCode).append(" ").append(responseCodeText).append(lineSeparator);
		for (String key : keySet)
		{
			sb.append(key).append(": ").append(headers.get(key)).append(lineSeparator);
		}
		return sb.toString();
	}
}