package com.ppm.server;

import com.ppm.server.http.HttpServer;

public class Main
{
	public static void main(String[] args)
	{
		HttpServer httpServer = new HttpServer();
		httpServer.start();
	}
}
