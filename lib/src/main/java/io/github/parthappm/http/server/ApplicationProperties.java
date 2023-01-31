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
	private final String SERVER_NAME;
	private final String HTTP_VERSION;
	private final String LOG_FILE_NAME_PREFIX;
	private final SimpleDateFormat RESPONSE_DATE_FORMAT;

	private ApplicationProperties()
	{
		Properties properties = new Properties();
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties"))
		{
			properties.load(is);
		}
		catch (IOException | NullPointerException ignored) {}
		this.SERVER_NAME = properties.getProperty("serverName");
		this.HTTP_VERSION = "HTTP/1.1";
		this.LOG_FILE_NAME_PREFIX = properties.getProperty("logFilePrefix");
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

	String serverName()
	{
		return SERVER_NAME == null ? "Nebula" : SERVER_NAME;
	}

	String httpVersion()
	{
		return HTTP_VERSION;
	}

	String timestampForResponse()
	{
		return RESPONSE_DATE_FORMAT.format(new Date());
	}

	String logFileName()
	{
		return LOG_FILE_NAME_PREFIX == null ? "" : LOG_FILE_NAME_PREFIX;
	}
}