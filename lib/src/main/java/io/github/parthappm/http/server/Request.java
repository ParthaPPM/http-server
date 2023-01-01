package io.github.parthappm.http.server;

import java.util.Map;

public record Request(String method, String path, Map<String, String> parameters, String anchor, Map<String, String> headers,
			   byte[] body)
{}