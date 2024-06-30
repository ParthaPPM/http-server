package io.github.parthappm.http.server.test;

import io.github.parthappm.http.server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		ServerProperties serverProperties = new ServerProperties();
		serverProperties.setPort(8080);
		serverProperties.setHost("127.0.0.1");
		serverProperties.setKeyStoreFileName("file name"); //TODO test this later
		serverProperties.setKeyStorePassword("password"); //TODO test this later
		serverProperties.setServerTimeoutInMillis(10000);
		serverProperties.setServerName("Test Server");
		serverProperties.setRootDirectory("");
		serverProperties.setLogFileName("log_file.txt");
		serverProperties.setLogDateFormat("[HH:mm:ss]");

		Server httpServer = new HttpServer(serverProperties);
		httpServer.addController("get", "/index.html", request -> new Response("Page found"));
		Server httpsServer = new HttpsServer(serverProperties);
		httpsServer.addController("get", "/index.html", request -> new Response("Page not found"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean continueExecution = true;
		System.out.println("Enter choice");
		do
		{
			switch (br.readLine())
			{
				case "start http" -> httpServer.start();
				case "start https" -> httpsServer.start();
				case "stop http" -> httpServer.stop();
				case "stop https" -> httpsServer.stop();
				case "stop" ->
				{
					httpServer.stop();
					httpsServer.stop();
					continueExecution = false;
				}
			}
		} while (continueExecution);
	}
}