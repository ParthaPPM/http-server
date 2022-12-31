package io.github.parthappm.http.server;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestProcessor
{
	public RequestProcessor()
	{}

	/* This method handles all the GET requests */
	public Response get(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the HEAD requests */
	public Response head(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the POST requests */
	public Response post(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the PUT requests */
	public Response put(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the DELETE requests */
	public Response delete(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the CONNECT requests */
	public Response connect(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the OPTIONS requests */
	public Response options(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the TRACE requests */
	public Response trace(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles all the PATCH requests */
	public Response patch(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}

	/* This method handles unknown requests */
	public Response none(String url, Map<String, String> requestParameters, Map<String, String> requestHeaders, byte[] requestBody)
	{
		return new Response(405, null, null);
	}
}
