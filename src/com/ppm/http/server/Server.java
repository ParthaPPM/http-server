package com.ppm.http.server;

public interface Server
{
	void setRequestProcessor(RequestProcessor requestProcessor);
	void start();
	void stop();
	boolean isServerRunning();
}