package com.ppm.server;

import com.ppm.server.http.HttpServer;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		HttpServer httpServer = new HttpServer();
		httpServer.start();
	}
}
