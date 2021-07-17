package com.ppm.server.http;

import java.util.Map;

public class RequestProcessor
{
	/* This method handles all the GET requests */
	public PartialResponse get(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the HEAD requests */
	public PartialResponse head(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the POST requests */
	public PartialResponse post(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the PUT requests */
	public PartialResponse put(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the DELETE requests */
	public PartialResponse delete(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the CONNECT requests */
	public PartialResponse connect(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the OPTIONS requests */
	public PartialResponse options(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the TRACE requests */
	public PartialResponse trace(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles all the PATCH requests */
	public PartialResponse patch(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}

	/* This method handles unknown requests */
	public PartialResponse none(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] body)
	{
		return new PartialResponse(405, null, null);
	}
}
