package io.github.parthappm.http.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Log
{
	private static Log currentObject;
	private final SimpleDateFormat dateFormat;
	private final File logFile;
	private final BlockingQueue<LogBean> logQueue;
	private final String INFO;
	private final String DEBUG;
	private final String ERROR;
	private final String STOP;

	private Log()
	{
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		this.logFile = new File(ApplicationProperties.getInstance().logFileName());
		this.logQueue = new LinkedBlockingQueue<>();
		this.INFO = "INFO";
		this.DEBUG = "DEBUG";
		this.ERROR = "ERROR";
		this.STOP = "STOP";

		// log consumer
		Thread consumer = new Thread(() -> {
			while (true)
			{
				try
				{
					LogBean logItem = logQueue.take();
					String logType = logItem.type();
					String logText = logItem.date() + " [" + logType + "] " + logItem.message();
					if (logType.equals(INFO) || logType.equals(ERROR))
					{
						System.out.println(logText);
					}
					writeToFile(logText);
					if (logType.equals(STOP))
					{
						break;
					}
				}
				catch (InterruptedException ignored) {}
			}
		});
		consumer.setName("consumer");
		consumer.start();
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
		catch (IOException ignored) {}
	}

	private void log(String logType, Object object)
	{
		Thread producer = new Thread(() -> {
			String logDate = dateFormat.format(new Date());
			try
			{
				if (object instanceof String message)
				{
					logQueue.put(new LogBean(logDate, logType, message));
				}
				else if (object instanceof Request request)
				{
					String statusLine = request.ip() + " " + request.method() + " " + request.path() + " " + request.anchor();
					StringBuilder parameters = new StringBuilder();
					StringBuilder headers = new StringBuilder();
					request.parameters().forEach((key, value) -> parameters.append(key).append(": ").append(value).append(System.lineSeparator()));
					request.headers().forEach((key, value) -> headers.append(key).append(": ").append(value).append(System.lineSeparator()));
					logQueue.put(new LogBean(logDate, INFO, statusLine));
					logQueue.put(new LogBean(logDate, DEBUG, "Request Parameters:"));
					logQueue.put(new LogBean(logDate, DEBUG, parameters.toString()));
					logQueue.put(new LogBean(logDate, DEBUG, "Request Headers:"));
					logQueue.put(new LogBean(logDate, DEBUG, headers.toString()));
					logQueue.put(new LogBean(logDate, DEBUG, "Request Body:"));
					logQueue.put(new LogBean(logDate, DEBUG, new String(request.body(), StandardCharsets.UTF_8)));
				}
				else if (object instanceof Response response)
				{
					String statusLine = response.statusCode() + " " + response.statusText();
					StringBuilder headers = new StringBuilder();
					response.headers().forEach((key, value) -> headers.append(key).append(": ").append(value).append(System.lineSeparator()));
					logQueue.put(new LogBean(logDate, INFO, statusLine));
					logQueue.put(new LogBean(logDate, DEBUG, "Response Headers:"));
					logQueue.put(new LogBean(logDate, DEBUG, headers.toString()));
					logQueue.put(new LogBean(logDate, DEBUG, "Response Body:"));
					logQueue.put(new LogBean(logDate, DEBUG, new String(response.body(), StandardCharsets.UTF_8)));
				}
				else if (object instanceof Exception exception)
				{
					StringWriter sw = new StringWriter();
					exception.printStackTrace(new PrintWriter(sw));
					logQueue.put(new LogBean(logDate, logType, sw.toString()));
				}
				else
				{
					logQueue.put(new LogBean(logDate, logType, object.toString()));
				}
			}
			catch (InterruptedException ignored) {}
		});
		producer.setName("producer");
		producer.start();
	}

	void info(Object object)
	{
		log(INFO, object);
	}

	void debug(Object object)
	{
		log(DEBUG, object);
	}

	void error(Object object)
	{
		log(ERROR, object);
	}

	void stop()
	{
		log(STOP, "Log produce stopped.");
	}
}
