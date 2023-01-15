package io.github.parthappm.http.server;

import java.util.Map;

/**
 * Request object will contain all the details of the HTTP request.
 * @param ip The ip address of the client.
 * @param method HTTP request method.
 * @param path The path or location part of the url.
 * @param parameters The request parameters as Map.
 * @param anchor The portion of the url after the #.
 * @param headers The request headers as Map.
 * @param body The request body as a byte array.
 */
public record Request(String ip, String method, String path, Map<String, String> parameters, String anchor, Map<String, String> headers,
			   byte[] body)
{}