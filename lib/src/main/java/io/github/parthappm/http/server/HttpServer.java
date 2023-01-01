package io.github.parthappm.http.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class HttpServer extends Server
{
	private final int port;
	private final String host;

	public HttpServer()
	{
		this(80);
	}

	public HttpServer(int port)
	{
		this(port, null);
	}

	public HttpServer(int port, String host)
	{
		this.port = port;
		this.host = host;
	}

	public void start()
	{
		System.out.println("Starting server at port: " + port);
		try
		{
			setServerSocket(new ServerSocket(port, 0, host == null ? null : InetAddress.getByName(host)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		super.start();
	}
}