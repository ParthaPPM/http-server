package io.github.parthappm.http.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;

/**
 * The HTTP server implementation for secured HTTP connection.
 */
public class HttpsServer extends Server
{
	private final int port;
	private final String host;
	private String keyStoreFileName;
	private String keyStorePassword;

	/**
	 * Creates an instance of HttpsServer class which listens to port 443 by default.
	 */
	public HttpsServer()
	{
		this(443);
	}

	/**
	 * Creates an instance of HttpsServer class which listens to the port specified.
	 * @param port The server listens to this port.
	 */
	public HttpsServer(int port)
	{
		this(port, null);
	}

	/**
	 * Creates an instance of HttpsServer class which listens to the port specified and accepts connection only from the host specified.
	 * @param port The server listens to this port.
	 * @param host The server accepts connection only from this host.
	 */
	public HttpsServer(int port, String host)
	{
		this.port = port;
		this.host = host;
	}

	/**
	 * Setter method to set the keystore file and the password to access it.
	 * Calling this function is important, without which the secured connection will not be created. As a result, no client program will be able to create a connection with the server.
	 * @param fileName The keystore file name.
	 * @param password The keystore file password.
	 * @return The reference of the current object for chaining.
	 */
	public HttpsServer setKeyStore(String fileName, String password)
	{
		this.keyStoreFileName = fileName;
		this.keyStorePassword = password;
		return this;
	}

	/**
	 * Start the server and start listening to new connection from clients.
	 */
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
