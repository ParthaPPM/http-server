package io.github.parthappm.http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class ConfigurationProperties
{
	private static ConfigurationProperties currentObject;
	private final String SERVER_NAME;
	private final Properties properties;
	private final String PROPERTY_KEY_STORE_FILE_NAME;
	private final String PROPERTY_KEY_STORE_PASSWORD;

	private ConfigurationProperties()
	{
		SERVER_NAME = "Nebula";
		File propertiesFile = new File(System.getProperty("user.dir") + "/server.config");
		PROPERTY_KEY_STORE_FILE_NAME = "keyStoreFileName";
		PROPERTY_KEY_STORE_PASSWORD = "password";

		properties = new Properties();
		try
		{
			InputStream propertiesInputStream = new FileInputStream(propertiesFile);
			properties.load(propertiesInputStream);
			propertiesInputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	static ConfigurationProperties getInstance()
	{
		if (currentObject == null)
		{
			currentObject = new ConfigurationProperties();
		}
		return currentObject;
	}

	String serverName()
	{
		return SERVER_NAME;
	}

	String keyStoreFileName()
	{
		return properties.getProperty(PROPERTY_KEY_STORE_FILE_NAME, "");
	}

	String keyStorePassword()
	{
		return properties.getProperty(PROPERTY_KEY_STORE_PASSWORD, "");
	}
}