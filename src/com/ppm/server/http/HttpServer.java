package com.ppm.server.http;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer
{
	private final int port;
	private final boolean showLog;
	private boolean isServerRunning;
	private ServerSocket serverSocket;

	public HttpServer()
	{
		this(80, true);
	}

	public HttpServer(int port)
	{
		this(port, true);
	}

	public HttpServer(boolean showLog)
	{
		this(80, showLog);
	}

	public HttpServer(int port, boolean showLog)
	{
		this.port = port;
		this.showLog = showLog;
		isServerRunning = false;
	}

	public void start()
	{
		System.out.println("Server started at port "+port);
		isServerRunning = true;
		try
		{
			serverSocket = new ServerSocket(port);
			while (isServerRunning)
			{
				Socket socket = serverSocket.accept();
				RequestHandler requestHandler = new RequestHandler(socket, showLog);
				Thread t = new Thread(requestHandler::handle);
				t.start();
			}
			serverSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			stop();
		}
	}

	public void stop()
	{
		isServerRunning = false;
		try
		{
			if (serverSocket!=null)
			{
				serverSocket.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean isServerRunning()
	{
		return isServerRunning;
	}
}