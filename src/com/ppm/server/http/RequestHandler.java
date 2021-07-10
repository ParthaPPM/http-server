package com.ppm.server.http;

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

public class RequestHandler
{
	private final Socket socket;
	private final boolean showLog;
	private final RequestProcessor requestProcessor;

	RequestHandler(Socket socket, boolean showLog)
	{
		this.socket = socket;
		this.showLog = showLog;
		this.requestProcessor = new RequestProcessor();
	}

	void handle()
	{
		try
		{
			OutputStream outputStream = socket.getOutputStream();
			Request request = getRequest(socket.getInputStream());
			String requestMethod = request.getMethod();
			if(showLog)
			{
				String date = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS:z").format(new Date());
				System.out.println("[" + date + "] " + socket.getInetAddress().getHostAddress() + " " + requestMethod + " " + request.getUrl());
			}
			PartialResponse partialResponse;
			switch (requestMethod)
			{
				case "GET":
					partialResponse = requestProcessor.get(request.getUrl(), request.getHeaders());
					break;
				case "HEAD":
					partialResponse = requestProcessor.head(request.getUrl(), request.getHeaders());
					break;
				case "POST":
					partialResponse = requestProcessor.post(request.getUrl(), request.getHeaders());
					break;
				case "PUT":
					partialResponse = requestProcessor.put(request.getUrl(), request.getHeaders());
					break;
				case "DELETE":
					partialResponse = requestProcessor.delete(request.getUrl(), request.getHeaders());
					break;
				case "CONNECT":
					partialResponse = requestProcessor.connect(request.getUrl(), request.getHeaders());
					break;
				case "OPTIONS":
					partialResponse = requestProcessor.options(request.getUrl(), request.getHeaders());
					break;
				case "TRACE":
					partialResponse = requestProcessor.trace(request.getUrl(), request.getHeaders());
					break;
				case "PATCH":
					partialResponse = requestProcessor.patch(request.getUrl(), request.getHeaders());
					break;
				default:
					partialResponse = requestProcessor.none(request.getUrl(), request.getHeaders());
			}
			Response response = generateResponse(partialResponse);
			sendResponse(outputStream, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			stop();
		}
	}

	private Request getRequest(InputStream inputStream)throws IOException
	{
		int totalBytesRead = 0;
		String method = "";
		String url = "";
		String version = "";
		Map<String, String> parametersMap = new HashMap<>();
		Map<String, String> headersMap = new HashMap<>();
		int bytesToRead = 0;
		byte[] body = null;
		StringBuilder line = new StringBuilder();
		boolean isFirstLine = true;

		//reading the request
		while(true)
		{
			int b = inputStream.read();
			if(b!=-1)
			{
				if(b!='\r')
				{
					line.append((char)b);
				}
				else
				{
					inputStream.read();
					if (line.toString().equals(""))
					{
						break;
					}
					if(isFirstLine)
					{
						isFirstLine = false;
						Scanner sc = new Scanner(line.toString());
						method = sc.next();
						String urlWithParameters = URLDecoder.decode(sc.next(), StandardCharsets.UTF_8.toString());
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
						String l = line.toString();
						int colonIndexPos = l.indexOf(':');
						String key = l.substring(0,colonIndexPos).trim();
						String value = l.substring(colonIndexPos+1).trim();
						headersMap.put(key, value);
						if (key.equals("Content-Length"))
						{
							bytesToRead = Integer.parseInt(value);
						}
					}
					line = new StringBuilder();
				}
			}
		}

		//reading the body
		if(bytesToRead>0)
		{
			body = new byte[bytesToRead];
			inputStream.read(body, 0, bytesToRead);
		}
		return new Request(method, url, version, parametersMap, headersMap, body);
	}

	private Response generateResponse(PartialResponse response)
	{
		String version = "HTTP/1.1";
		int responseCode = response.getResponseCode();
		String responseCodeText;
		Map<String, String> headersMap = new HashMap<>();
		Map<String, String> customHeaders = response.getCustomHeaders();
		byte[] body = response.getBody();

		switch (responseCode)
		{
			case 200:
				responseCodeText = "OK";
				break;
			case 403:
				responseCodeText = "Forbidden";
				break;
			case 404:
				responseCodeText = "Not Found";
				break;
			case 405:
				responseCodeText = "Method Not Allowed";
				break;
			case 500:
				responseCodeText = "Internal Server Error";
				break;
			default:
				responseCodeText = "Unknown code";
		}

		headersMap.put("Date", (new Date()).toString());
		headersMap.put("Server", "Nebula");
		headersMap.put("Connection", "close");
		if(customHeaders!=null)
		{
			headersMap.putAll(customHeaders);
		}
		if(body!=null)
		{
			headersMap.put("Content-Length", String.valueOf(body.length));
		}

		return new Response(version, responseCode, responseCodeText, headersMap, body);
	}

	private void sendResponse(OutputStream outputStream, Response response) throws IOException
	{
		PrintWriter pw = new PrintWriter(outputStream, true);
		pw.println(response.getVersion()+" "+response.getResponseCode()+" "+response.getResponseCodeText());
		Map<String, String> headers = response.getHeaders();
		for(String key : headers.keySet())
		{
			pw.println(key+": "+headers.get(key));
		}
		pw.println();
		outputStream.write(response.getBody());
		outputStream.flush();
		outputStream.close();
		socket.close();
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
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}