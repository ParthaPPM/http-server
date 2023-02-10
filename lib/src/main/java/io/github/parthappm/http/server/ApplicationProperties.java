package io.github.parthappm.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

class ApplicationProperties
{
	private static ApplicationProperties currentProperties;
	private final int DEFAULT_HTTP_PORT;
	private final int DEFAULT_HTTPS_PORT;
	private final int SERVER_TIMEOUT_IN_MILLIS;
	private final String HTTP_VERSION;
	private final String SERVER_NAME;
	private final String LOG_FILE_NAME_PREFIX;
	private final SimpleDateFormat RESPONSE_DATE_FORMAT;

	private ApplicationProperties()
	{
		Properties properties = new Properties();
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties"))
		{
			properties.load(is);
		}
		catch (IOException | NullPointerException e)
		{
			Log.getInstance().debug("File: application.properties not found.");
		}

		this.DEFAULT_HTTP_PORT = 80;
		this.DEFAULT_HTTPS_PORT = 443;
		this.SERVER_TIMEOUT_IN_MILLIS = 30000; // 30 seconds
		this.HTTP_VERSION = "HTTP/1.1";
		this.SERVER_NAME = properties.getProperty("serverName", "");
		this.LOG_FILE_NAME_PREFIX = properties.getProperty("logFilePrefix", "");
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

	int defaultHttpPort()
	{
		return this.DEFAULT_HTTP_PORT;
	}

	int defaultHttpsPort()
	{
		return this.DEFAULT_HTTPS_PORT;
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
		return SERVER_NAME.equals("") ? "Nebula" : SERVER_NAME;
	}

	String timestampForResponse()
	{
		return RESPONSE_DATE_FORMAT.format(new Date());
	}

	String logFileName()
	{
		return LOG_FILE_NAME_PREFIX.equals("") ? "" : LOG_FILE_NAME_PREFIX + ".log";
	}
}