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
	 * Creates a ServerProperties object with default settings.
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

	/**
	 * Sets the HTTP/HTTPS port in which the server will server.
	 * By default, 80 of HTTP and 443 for HTTPS.
	 * @param port HTTP/HTTPS port.
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * Returns the HTTP/HTTPS port.
	 * @return HTTP/HTTPS port.
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Sets the host of the server. If the server machine is connected to more than one network, this host identified in which network to serve.
	 * By default, it is null, which means the server can serve request from any client from any network.
	 * @param host Host name.
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * Returns the host name.
	 * @return Host name.
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Sets the KeyStore (.jks) file required for HTTPS server.
	 * @param keyStoreFileName KeyStore file name with absolute or relative path.
	 */
	public void setKeyStoreFileName(String keyStoreFileName)
	{
		this.keyStoreFileName = keyStoreFileName;
	}

	/**
	 * Returns the KeyStore(.jks) file name.
	 * @return KeyStore file name with absolute or relative path.
	 */
	public String getKeyStoreFileName()
	{
		return keyStoreFileName;
	}

	/**
	 * Sets the password of the KeyStore file. This password was set while creating the KeyStore file.
	 * @param keyStorePassword Password of the KeyStore file.
	 */
	public void setKeyStorePassword(String keyStorePassword)
	{
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * Returns the password of the KeyStore file.
	 * @return Password of KeyStore file.
	 */
	public String getKeyStorePassword()
	{
		return keyStorePassword;
	}

	/**
	 * Sets the server timeout. By default, timeout is 30 seconds i.e. 30000 milliseconds.
	 * @param serverTimeoutInMillis Time in milliseconds.
	 */
	public void setServerTimeoutInMillis(int serverTimeoutInMillis)
	{
		this.serverTimeoutInMillis = serverTimeoutInMillis;
	}

	/**
	 * Returns the server timeout in milliseconds.
	 * @return Time in milliseconds.
	 */
	public int getServerTimeoutInMillis()
	{
		return serverTimeoutInMillis;
	}

	/**
	 * Returns the HTTP version. E.g: HTTP/1.1
	 * @return HTTP version.
	 */
	public String getHttpVersion()
	{
		return httpVersion;
	}

	/**
	 * Sets the Server Name that will be sent in the HTTP response headers.
	 * The server name can be anything. This helps the client to identify which server has server the response. By default, the server name is "Nebula".
	 * @param serverName HTTP server name.
	 */
	public void setServerName(String serverName)
	{
		if (serverName != null && !serverName.isEmpty())
		{
			this.serverName = serverName;
		}
	}

	/**
	 * Returns the HTTP server name.
	 * @return HTTP server name.
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * Sets the default directory from which the HTTP files will be servers.
	 * By default, root directory is null which means no files will be served from any location.
	 * @param rootDirectory Path to directory from where files will be served.
	 */
	public void setRootDirectory(String rootDirectory)
	{
		this.rootDirectory = rootDirectory;
	}

	/**
	 * Returns the default directory from which the HTTP files will be served.
	 * @return Path to directory from where file will be served.
	 */
	public String getRootDirectory()
	{
		return rootDirectory;
	}

	/**
	 * Sets the Log file name where all the logs will be dumped. This can be helpful in debugging.
	 * By default, the file name is null, which means no log file is created.
	 * @param fileName Log file name.
	 */
	public void setLogFileName(String fileName)
	{
		this.logFileName = fileName;
	}

	/**
	 * Returns the Log file name.
	 * @return Log file name.
	 */
	public String getLogFileName()
	{
		return logFileName;
	}

	/**
	 * Sets the date format string pattern to be used for logging in console or in log file.
	 * @param logDateFormat String pattern that will be parsed by SimpleDateFormat class.
	 */
	public void setLogDateFormat(String logDateFormat)
	{
		this.logDateFormat = new SimpleDateFormat(logDateFormat);
	}

	/**
	 * Returns the instance of SimpleDateFormat class that is used to format date for logging.
	 * @return SimpleDateFormat object
	 */
	SimpleDateFormat getLogDateFormat()
	{
		return logDateFormat;
	}

	/**
	 * Returns the instance of SimpleDateFormat class that is used to format date for returning in the HTTP response header
	 * @return SimpleDateFormat object
	 */
	SimpleDateFormat getResponseDateFormat()
	{
		return responseDateFormat;
	}
}