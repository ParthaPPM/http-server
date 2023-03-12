package io.github.parthappm.http.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class ApplicationProperties
{
	private static ApplicationProperties currentProperties;
	private final int HTTP_PORT;
	private final int HTTPS_PORT;
	private final int SERVER_TIMEOUT_IN_MILLIS;
	private final String HTTP_VERSION;
	private String serverName;
	private String logFileName;
	private boolean logResponse;
	private final SimpleDateFormat RESPONSE_DATE_FORMAT;

	private ApplicationProperties()
	{
		this.HTTP_PORT = 80;
		this.HTTPS_PORT = 443;
		this.SERVER_TIMEOUT_IN_MILLIS = 30000; // 30 seconds
		this.HTTP_VERSION = "HTTP/1.1";
		this.serverName = "Nebula";
		this.logFileName = null;
		this.logResponse = false;
		this.RESPONSE_DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		this.RESPONSE_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	static ApplicationProperties getInstance()
	{
		if (currentProperties == null)
		{
			currentProperties = new ApplicationProperties();
		}
		return currentProperties;
	}

	void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	void setLogFileName(String fileName)
	{
		this.logFileName = fileName;
	}

	void setLogResponse(boolean flag)
	{
		this.logResponse = flag;
	}

	int httpPort()
	{
		return this.HTTP_PORT;
	}

	int httpsPort()
	{
		return this.HTTPS_PORT;
	}

	int serverTimeout()
	{
		return this.SERVER_TIMEOUT_IN_MILLIS;
	}

	String httpVersion()
	{
		return HTTP_VERSION;
	}

	String serverName()
	{
		return serverName;
	}

	String timestampForResponse()
	{
		return RESPONSE_DATE_FORMAT.format(new Date());
	}

	String logFileName()
	{
		return logFileName;
	}

	boolean logResponse()
	{
		return logResponse;
	}
}