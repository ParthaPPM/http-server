package io.github.parthappm.http.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

class Log
{
	private static Log currentObject;
	private final SimpleDateFormat dateFormat;
	private final File logFile;

	private Log()
	{
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		this.logFile = new File(ApplicationProperties.getInstance().logFileName());
	}

	static Log getInstance()
	{
		if (currentObject == null)
		{
			currentObject = new Log();
		}
		return currentObject;
	}

	private void writeToFile(String logText)
	{
		try(FileOutputStream fos = new FileOutputStream(logFile, true))
		{
			fos.write(logText.getBytes(StandardCharsets.UTF_8));
			fos.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void info(String message)
	{
		System.out.println(message);
	}

	void warn(String message)
	{}

	void debug(Exception e)
	{
	}

	void error(String message)
	{}

	void error(Exception e)
	{
		e.printStackTrace();
	}

	void log(String message)
	{
		String date = dateFormat.format(new Date());
		System.out.println("[" + date + "] " + message);
		writeToFile(message);
	}
}
