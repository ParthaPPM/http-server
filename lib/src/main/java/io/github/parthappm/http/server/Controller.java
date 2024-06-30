package io.github.parthappm.http.server;

public interface Controller
{
	Response process(Request request);
}