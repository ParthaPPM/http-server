package io.github.parthappm.http.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Logger
{
	private enum LogType
	{
		INFO, DEBUG, ERROR, STOP
	}

	private record LogItem(LogType type, Date date, Object message)
	{}

	private final String lineSeparator;
	private final String logFileName;
	private final SimpleDateFormat logDateFormat;
	private final BlockingQueue<LogItem> logQueue;
	private static Logger currentLogger;

	private Logger(ServerProperties properties)
	{
		this.lineSeparator = System.lineSeparator();
		this.logFileName = properties.getLogFileName();
		this.logDateFormat = properties.getLogDateFormat();
		this.logQueue = new LinkedBlockingQueue<>();
		new Thread(this::exportLogEntry).start();
	}

	private String getLogMessage(Object object)
	{
		if (object instanceof String message)
		{
			return message;
		}
		else if (object instanceof Exception exception)
		{
			StringWriter message = new StringWriter();
			exception.printStackTrace(new PrintWriter(message));
			return message.toString();
		}
		else if (object instanceof Map<?, ?> map)
		{
			StringJoiner message = new StringJoiner(lineSeparator);
			map.forEach((key, value) -> message.add(key + ": " + value));
			return message.toString();
		}
		else
		{
			return object.toString();
		}
	}

	private void writeToFile(String logText)
	{
		try(FileOutputStream fos = new FileOutputStream(logFileName, true))
		{
			fos.write(logText.getBytes(StandardCharsets.UTF_8));
			fos.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
		}
		catch (NullPointerException | IOException ignored) {}
	}

	private void exportLogEntry()
	{
		// consuming the log
		while (true)
		{
			try
			{
				LogItem logItem = logQueue.take();
				switch (logItem.type)
				{
					case INFO:
					{
						String logText = logDateFormat.format(logItem.date()) + " [INFO] " + getLogMessage(logItem.message);
						System.out.println(logText);
						writeToFile(logText);
						break;
					}
					case DEBUG:
					{
						String logText = logDateFormat.format(logItem.date()) + " [DEBUG] " + getLogMessage(logItem.message);
						writeToFile(logText);
						break;
					}
					case ERROR:
					{
						String logText = logDateFormat.format(logItem.date()) + " [ERROR] " + getLogMessage(logItem.message);
						System.out.println(logText);
						writeToFile(logText);
						break;
					}
				}
				// breaking the while loop if the LOG type is STOP
				if (logItem.type == LogType.STOP)
				{
					break;
				}
			}
			catch (InterruptedException ignore) {}
		}
	}

	private void createLogEntry(LogType logType, Object object)
	{
		// producing the log
		try
		{
			logQueue.put(new LogItem(logType, new Date(), object));
		}
		catch (InterruptedException | ClassCastException | NullPointerException | IllegalArgumentException ignore) {}
	}

	static Logger getInstance(ServerProperties properties)
	{
		if (currentLogger == null)
		{
			currentLogger = new Logger(properties);
		}
		return currentLogger;
	}

	void info(Object object)
	{
		createLogEntry(LogType.INFO, object);
	}

	void debug(Object object)
	{
		createLogEntry(LogType.DEBUG, object);
	}

	void error(Object object)
	{
		createLogEntry(LogType.ERROR, object);
	}

	void stop()
	{
		createLogEntry(LogType.STOP, null);
	}
}
