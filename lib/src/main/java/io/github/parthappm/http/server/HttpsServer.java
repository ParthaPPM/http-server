package io.github.parthappm.http.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;

public class HttpsServer extends Server
{
	private final int port;
	private final String host;
	private String keyStoreFileName;
	private String keyStorePassword;

	public HttpsServer()
	{
		this(443);
	}

	public HttpsServer(int port)
	{
		this(port, null);
	}

	public HttpsServer(int port, String host)
	{
		this.port = port;
		this.host = host;
	}

	public HttpsServer setKeyStore(String fileName, String password)
	{
		this.keyStoreFileName = fileName;
		this.keyStorePassword = password;
		return this;
	}

	public void start()
	{
		System.out.println("Starting server at port: " + port);
		System.setProperty("javax.net.ssl.keyStore", keyStoreFileName);
		System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		ServerSocketFactory socketFactory = SSLServerSocketFactory.getDefault();
		try
		{
			setServerSocket(socketFactory.createServerSocket(port, 0, host == null ? null : InetAddress.getByName(host)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		super.start();
	}
}
