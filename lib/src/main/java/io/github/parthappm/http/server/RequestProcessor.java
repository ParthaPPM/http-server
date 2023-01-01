package io.github.parthappm.http.server;

public class RequestProcessor
{
	public RequestProcessor()
	{}

	/* This method handles all the GET requests */
	public Response get(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the HEAD requests */
	public Response head(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the POST requests */
	public Response post(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the PUT requests */
	public Response put(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the DELETE requests */
	public Response delete(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the CONNECT requests */
	public Response connect(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the OPTIONS requests */
	public Response options(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the TRACE requests */
	public Response trace(Request request)
	{
		return new Response(405);
	}

	/* This method handles all the PATCH requests */
	public Response patch(Request request)
	{
		return new Response(405);
	}

	/* This method handles unknown requests */
	public Response none(Request request)
	{
		return new Response(405);
	}
}
