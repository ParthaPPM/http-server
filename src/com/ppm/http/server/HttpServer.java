package com.ppm.http.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Server
{
	private final int port;
	private final String host;
	private boolean isServerRunning;
	private ServerSocket serverSocket;
	private RequestProcessor requestProcessor;
	private int timeoutInMilliSeconds;

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
		this.isServerRunning = false;
		this.requestProcessor = new RequestProcessor();
		this.timeoutInMilliSeconds = 60000;
	}

	public void setRequestProcessor(RequestProcessor requestProcessor)
	{
		this.requestProcessor = requestProcessor;
	}

	public void setTimeout(int milliSeconds)
	{
		this.timeoutInMilliSeconds = milliSeconds;
	}

	public void start()
	{
		System.out.println("Server started at port "+port);
		isServerRunning = true;
		try
		{
			if (host == null)
			{
				serverSocket = new ServerSocket(port);
			}
			else
			{
				serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host));
			}
			while (isServerRunning)
			{
				Socket socket = serverSocket.accept();
				RequestHandler requestHandler = new RequestHandler(socket, requestProcessor, timeoutInMilliSeconds);
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