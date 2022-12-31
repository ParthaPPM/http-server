package io.github.parthappm.http.server;

public interface Server
{
	void setRequestProcessor(RequestProcessor requestProcessor);
	void setTimeout(int milliSeconds);
	void start();
	void stop();
	boolean isServerRunning();
}