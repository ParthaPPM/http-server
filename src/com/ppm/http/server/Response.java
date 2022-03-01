package com.ppm.http.server;

import java.util.Map;

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
}