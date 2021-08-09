package com.ppm.http.server;

import java.util.Map;

public class PartialResponse
{
	private final int responseCode;
	private final Map<String, String> customHeaders;
	private final byte[] body;

	public PartialResponse()
	{
		this(404, null, null);
	}

	public PartialResponse(byte[] body)
	{
		this(200, null, body);
	}

	public PartialResponse(Map<String, String> customHeaders, byte[] body)
	{
		this(200, customHeaders, body);
	}

	public PartialResponse(int responseCode, Map<String, String> customHeaders)
	{
		this(responseCode, customHeaders, null);
	}

	public PartialResponse(int responseCode, byte[] body)
	{
		this(responseCode, null, body);
	}

	public PartialResponse(int responseCode, Map<String, String> customHeaders, byte[] body)
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

	public int getResponseCode()
	{
		return responseCode;
	}

	public Map<String, String> getCustomHeaders()
	{
		return customHeaders;
	}

	public byte[] getBody()
	{
		return body;
	}
}