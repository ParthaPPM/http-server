package io.github.parthappm.http.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.InetAddress;

/**
 * The HTTP server implementation for secured HTTP connection.
 */
public class HttpsServer extends Server
{
	/**
	 * Creates an instance of HttpsServer class with default properties.
	 */
	public HttpsServer()
	{
		this(new ServerProperties());
		properties.setPort(443);
	}

	/**
	 * Creates an instance of HttpsServer class with custom properties.
	 * @param properties An instance of ServerProperties class.
	 */
	public HttpsServer(ServerProperties properties)
	{
		super(properties);
	}

	/**
	 * Start the server and start listening to new connection from clients.
	 */
	public void start()
	{
		try
		{
			System.setProperty("javax.net.ssl.keyStore", properties.getKeyStoreFileName());
			System.setProperty("javax.net.ssl.keyStorePassword", properties.getKeyStorePassword());
			ServerSocketFactory socketFactory = SSLServerSocketFactory.getDefault();
			serverSocket = socketFactory.createServerSocket(properties.getPort(), 0, properties.getHost() == null ? null : InetAddress.getByName(properties.getHost()));
		}
		catch (Exception e)
		{
			logger.error(e);
		}
		super.start();
	}
}