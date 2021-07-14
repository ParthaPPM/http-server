package com.ppm.server.http;

import java.util.Map;

class PartialResponse
{
	private final int responseCode;
	private final Map<String, String> customHeaders;
	private final byte[] body;

	PartialResponse()
	{
		this(404, null, null);
	}

	PartialResponse(byte[] body)
	{
		this(200, null, body);
	}

	PartialResponse(Map<String, String> customHeaders, byte[] body)
	{
		this(200, customHeaders, body);
	}

	PartialResponse(int responseCode, Map<String, String> customHeaders)
	{
		this(responseCode, customHeaders, null);
	}

	PartialResponse(int responseCode, byte[] body)
	{
		this(responseCode, null, body);
	}

	PartialResponse(int responseCode, Map<String, String> customHeaders, byte[] body)
	{
		this.responseCode = responseCode;
		if (customHeaders != null && customHeaders.size() == 0)
		{
			this.customHeaders = null;
		}
		else
		{
			this.customHeaders = customHeaders;
		}
		if (body != null && body.length == 0)
		{
			this.body = null;
		}
		else
		{
			this.body = body;
		}
	}

	int getResponseCode()
	{
		return responseCode;
	}

	Map<String, String> getCustomHeaders()
	{
		return customHeaders;
	}

	byte[] getBody()
	{
		return body;
	}
}