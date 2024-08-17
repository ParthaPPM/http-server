package io.github.parthappm.http.server;

/**
 * Functional Interface to add controller for a request.
 */
public interface Controller
{
	/**
	 * This method actually processes the request and returns a response object as per the implementation.
	 * @param request Request object containing all the details of the HTTP request.
	 * @return Response object containing all the details of the HTTP response that will be returned to the client.
	 */
	Response process(Request request);
}