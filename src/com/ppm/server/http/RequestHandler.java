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

	RequestHandler(Socket socket, boolean showLog)
	{
		this.socket = socket;
		this.showLog = showLog;
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
			switch (requestMethod)
			{
				case "GET":
					sendResponse(outputStream, handleGetRequest(request));
					break;
				case "HEAD":
					sendResponse(outputStream, handleHeadRequest(request));
					break;
				case "POST":
					sendResponse(outputStream, handlePostRequest(request));
					break;
				case "PUT":
					sendResponse(outputStream, handlePutRequest(request));
					break;
				case "DELETE":
					sendResponse(outputStream, handleDeleteRequest(request));
					break;
				case "CONNECT":
					sendResponse(outputStream, handleConnectRequest(request));
					break;
				case "OPTIONS":
					sendResponse(outputStream, handleOptionsRequest(request));
					break;
				case "TRACE":
					sendResponse(outputStream, handleTraceRequest(request));
					break;
				case "PATCH":
					sendResponse(outputStream, handlePatchRequest(request));
					break;
				default:
					sendResponse(outputStream, generateResponse(405, null, null));
			}
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

	/* This method handles all the GET requests */
	private Response handleGetRequest(Request request)
	{
		switch (request.getUrl())
		{
			case "/":
			{
				byte[] body = "hello user".getBytes(StandardCharsets.UTF_8);
				return generateResponse(200, null, body);
			}
			default:
			{
				return generateResponse(404, null, null);
			}
		}
	}

	/* This method handles all the HEAD requests */
	private Response handleHeadRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the POST requests */
	private Response handlePostRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the PUT requests */
	private Response handlePutRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the DELETE requests */
	private Response handleDeleteRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the CONNECT requests */
	private Response handleConnectRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the OPTIONS requests */
	private Response handleOptionsRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the TRACE requests */
	private Response handleTraceRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	/* This method handles all the PATCH requests */
	private Response handlePatchRequest(Request request)
	{
		return generateResponse(500, null, null);
	}

	private Request getRequest(InputStream inputStream)throws IOException
	{
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

	private Response generateResponse(int responseCode, Map<String, String> extraHeaders, byte[] body)
	{
		String version = "HTTP/1.1";
		String responseCodeText;
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
				responseCodeText = "Bad Request";
		}
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Date", (new Date()).toString());
		if(extraHeaders!=null && extraHeaders.size()!=0)
		{
			headerMap.putAll(extraHeaders);
		}
		if(body!=null)
		{
			headerMap.put("Content-Length", String.valueOf(body.length));
		}
		headerMap.put("Server", "Nebula");
		headerMap.put("Connection", "close");
		return new Response(version, responseCode, responseCodeText, headerMap, body);
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