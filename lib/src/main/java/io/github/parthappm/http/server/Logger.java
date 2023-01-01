package io.github.parthappm.http.server;

import java.text.SimpleDateFormat;
import java.util.Date;

class Logger
{
	static Logger currentObject;
	private final SimpleDateFormat dateFormat;

	private Logger()
	{
		dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS:z");
	}

	static Logger getInstance()
	{
		if (currentObject == null)
		{
			currentObject = new Logger();
		}
		return currentObject;
	}

	void log(String message)
	{
		String date = dateFormat.format(new Date());
		System.out.println("[" + date + "] " + message);
	}
}
