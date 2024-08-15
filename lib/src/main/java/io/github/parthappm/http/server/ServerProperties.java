package io.github.parthappm.http.server;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * This Properties class contains all the properties required for the server.
 */
public class ServerProperties
{
	private int port;
	private String host;
	private String keyStoreFileName;
	private String keyStorePassword;
	private int serverTimeoutInMillis;
	private final String httpVersion;
	private String serverName;
	private String rootDirectory;
	private String logFileName;
	private SimpleDateFormat logDateFormat;
	private final SimpleDateFormat responseDateFormat;

	/**
	 * Creates an instance of ServerProperties class which listens to port 443 by default.
	 */
	public ServerProperties()
	{
		this.port = 80;
		this.host = null;
		this.keyStoreFileName = null;
		this.keyStorePassword = null;
		this.serverTimeoutInMillis = 30000; // 30 seconds
		this.httpVersion = "HTTP/1.1";
		this.serverName = "Nebula";
		this.rootDirectory = null;
		this.logFileName = null;
		this.logDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		this.responseDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

		this.responseDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getPort()
	{
		return port;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getHost()
	{
		return host;
	}

	public void setKeyStoreFileName(String keyStoreFileName)
	{
		this.keyStoreFileName = keyStoreFileName;
	}

	public String getKeyStoreFileName()
	{
		return keyStoreFileName;
	}

	public void setKeyStorePassword(String keyStorePassword)
	{
		this.keyStorePassword = keyStorePassword;
	}

	public String getKeyStorePassword()
	{
		return keyStorePassword;
	}

	public void setServerTimeoutInMillis(int serverTimeoutInMillis)
	{
		this.serverTimeoutInMillis = serverTimeoutInMillis;
	}

	public int getServerTimeoutInMillis()
	{
		return serverTimeoutInMillis;
	}

	public String getHttpVersion()
	{
		return httpVersion;
	}

	public void setServerName(String serverName)
	{
		if (serverName != null && !serverName.isEmpty())
		{
			this.serverName = serverName;
		}
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setRootDirectory(String rootDirectory)
	{
		this.rootDirectory = rootDirectory;
	}

	public String getRootDirectory()
	{
		return rootDirectory;
	}

	public void setLogFileName(String fileName)
	{
		this.logFileName = fileName;
	}

	public String getLogFileName()
	{
		return logFileName;
	}

	public void setLogDateFormat(String logDateFormat)
	{
		this.logDateFormat = new SimpleDateFormat(logDateFormat);
	}

	SimpleDateFormat getLogDateFormat()
	{
		return logDateFormat;
	}

	SimpleDateFormat getResponseDateFormat()
	{
		return responseDateFormat;
	}
}