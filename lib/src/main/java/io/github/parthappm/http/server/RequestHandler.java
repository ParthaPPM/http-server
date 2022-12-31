package io.github.parthappm.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class RequestHandler
{
	private final Socket socket;
	private boolean closeConnection;
	private final RequestProcessor requestProcessor;
	private final int timeoutInMilliSeconds;

	RequestHandler(Socket socket, RequestProcessor requestProcessor, int timeoutInMilliSeconds)
	{
		this.socket = socket;
		this.closeConnection = false;
		this.requestProcessor = requestProcessor;
		this.timeoutInMilliSeconds = timeoutInMilliSeconds;
	}

	void handle()
	{
		try
		{
			socket.setSoTimeout(timeoutInMilliSeconds);
			OutputStream outputStream = socket.getOutputStream();
			Request request = getRequest(socket.getInputStream());
			String requestMethod = request.method();
			// Logging starts, should be removed while implementing the logging logic
			String date = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS:z").format(new Date());
			System.out.println("[" + date + "] " + socket.getInetAddress().getHostAddress() + " " + requestMethod + " " + request.url());
			// Logging ends, should be removed while implementing the logging logic
			Response response = switch (requestMethod)
			{
				case "GET" -> requestProcessor.get(request.url(), request.parameters(), request.headers(), request.body());
				case "HEAD" -> requestProcessor.head(request.url(), request.parameters(), request.headers(), request.body());
				case "POST" -> requestProcessor.post(request.url(), request.parameters(), request.headers(), request.body());
				case "PUT" -> requestProcessor.put(request.url(), request.parameters(), request.headers(), request.body());
				case "DELETE" -> requestProcessor.delete(request.url(), request.parameters(), request.headers(), request.body());
				case "CONNECT" -> requestProcessor.connect(request.url(), request.parameters(), request.headers(), request.body());
				case "OPTIONS" -> requestProcessor.options(request.url(), request.parameters(), request.headers(), request.body());
				case "TRACE" -> requestProcessor.trace(request.url(), request.parameters(), request.headers(), request.body());
				case "PATCH" -> requestProcessor.patch(request.url(), request.parameters(), request.headers(), request.body());
				default -> requestProcessor.none(request.url(), request.parameters(), request.headers(), request.body());
			};
			sendResponse(outputStream, response);
		}
		catch (Exception e)
		{
			stop();
		}
	}

	private Request getRequest(InputStream inputStream)throws IOException
	{
		String method = "";
		String url = "";
		String version = "";
		Map<String, String> parametersMap = new HashMap<>();
		Map<String, String> headersMap = new HashMap<>();
		int bytesToRead = 0;
		StringBuilder line = new StringBuilder();
		boolean isFirstLine = true;

		//reading the request character by character
		while(true)
		{
			int b = inputStream.read();
			if(b!='\r')
			{
				line.append((char)b);
			}
			else
			{
				inputStream.read();
				String l = line.toString();
				if (l.equals(""))
				{
					break;
				}
				if(isFirstLine)
				{
					isFirstLine = false;
					Scanner sc = new Scanner(l);
					method = sc.next();
					String urlWithParameters = URLDecoder.decode(sc.next(), StandardCharsets.UTF_8);
					version = sc.next();

					int indexOfQuestionMark = urlWithParameters.indexOf('?');
					if (indexOfQuestionMark == -1)
					{
						url = urlWithParameters;
					}
					else
					{
						url = urlWithParameters.substring(0, indexOfQuestionMark);
						String parameters = urlWithParameters.substring(indexOfQuestionMark + 1);
						sc = new Scanner(parameters);
						sc.useDelimiter("&");
						while (sc.hasNext())
						{
							String s = sc.next();
							int indexOfEquals = s.indexOf('=');
							String key = s.substring(0, indexOfEquals).trim();
							String value = s.substring(indexOfEquals + 1).trim();
							parametersMap.put(key, value);
						}
					}
				}
				else
				{
					int colonIndexPos = l.indexOf(':');
					String key = l.substring(0, colonIndexPos).trim();
					String value = l.substring(colonIndexPos + 1).trim();
					headersMap.put(key, value);
					if (key.equals("Content-Length"))
					{
						bytesToRead = Integer.parseInt(value);
					}
					else if(key.equals("Connection"))
					{
						if(value.equals("close"))
						{
							this.closeConnection = true;
						}
					}
				}
				line = new StringBuilder();
			}
		}

		//reading the body
		byte[] body = new byte[bytesToRead];
		inputStream.read(body, 0, bytesToRead);
		return new Request(method, url, version, parametersMap, headersMap, body);
	}

	private void sendResponse(OutputStream outputStream, Response response) throws IOException
	{
		PrintWriter pw = new PrintWriter(outputStream, true);
		pw.println(response.version()+" "+response.responseCode()+" "+response.responseCodeText());
		Map<String, String> headers = response.headers();
		for(String key : headers.keySet())
		{
			pw.println(key+": "+headers.get(key));
		}
		pw.println();
		if(response.body()!=null)
		{
			outputStream.write(response.body());
		}

		if(this.closeConnection)
		{
			stop();
		}
		else
		{
			handle();
		}
	}

	void stop()
	{
		try
		{
			if (socket!=null)
			{
				socket.close();
			}
		}
		catch (Exception ignored) {}
	}
}