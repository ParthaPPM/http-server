package io.github.parthappm.http.server;

/**
 * The RequestProcessor class which contains the logic to process the request and create the response.
 * This class has to be extended while creating the custom RequestProcessor and passed to the Server object.
 */
public class RequestProcessor
{
	/**
	 * Create and instance of this class.
	 */
	public RequestProcessor()
	{}

	/**
	 * This method handles all the GET requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response get(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the HEAD requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response head(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the POST requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response post(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the PUT requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response put(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the DELETE requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response delete(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the CONNECT requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response connect(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the OPTIONS requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response options(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the TRACE requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response trace(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles all the PATCH requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response patch(Request request)
	{
		return new Response(405);
	}

	/**
	 * This method handles any other type of HTTP request methods.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response none(Request request)
	{
		return new Response(405);
	}
}
