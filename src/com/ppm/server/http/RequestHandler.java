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
	private boolean closeConnection;
	private final boolean showLog;
	private final RequestProcessor requestProcessor;

	RequestHandler(Socket socket, boolean showLog)
	{
		this.socket = socket;
		this.closeConnection = false;
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
					partialResponse = requestProcessor.get(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "HEAD":
					partialResponse = requestProcessor.head(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "POST":
					partialResponse = requestProcessor.post(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "PUT":
					partialResponse = requestProcessor.put(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "DELETE":
					partialResponse = requestProcessor.delete(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "CONNECT":
					partialResponse = requestProcessor.connect(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "OPTIONS":
					partialResponse = requestProcessor.options(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "TRACE":
					partialResponse = requestProcessor.trace(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				case "PATCH":
					partialResponse = requestProcessor.patch(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
					break;
				default:
					partialResponse = requestProcessor.none(request.getUrl(), request.getParameters(), request.getHeaders(), request.getBody());
			}
			Response response = generateResponse(partialResponse);
			sendResponse(outputStream, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		byte[] body = null;
		StringBuilder line = new StringBuilder();
		boolean isFirstLine = true;

		//reading the request character by character
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
			case 100:
				responseCodeText = "Continue";
				break;
			case 101:
				responseCodeText = "Switching Protocol";
				break;
			case 102:
				responseCodeText = "Processing";
				break;
			case 103:
				responseCodeText = "Early Hints";
				break;
			case 200:
				responseCodeText = "OK";
				break;
			case 201:
				responseCodeText = "Created";
				break;
			case 202:
				responseCodeText = "Accepted";
				break;
			case 203:
				responseCodeText = "Non-Authoritative Information";
				break;
			case 204:
				responseCodeText = "No Content";
				break;
			case 205:
				responseCodeText = "Reset Content";
				break;
			case 206:
				responseCodeText = "Partial Content";
				break;
			case 207:
				responseCodeText = "Multi-Status";
				break;
			case 208:
				responseCodeText = "Already Reported";
				break;
			case 226:
				responseCodeText = "IM Used";
				break;
			case 300:
				responseCodeText = "Multiple Choice";
				break;
			case 301:
				responseCodeText = "Moved Permanently";
				break;
			case 302:
				responseCodeText = "Found";
				break;
			case 303:
				responseCodeText = "See Other";
				break;
			case 304:
				responseCodeText = "Not Modified";
				break;
			case 305:
				responseCodeText = "Use Proxy";
				break;
			case 306:
				responseCodeText = "unused";
				break;
			case 307:
				responseCodeText = "Temporary Redirect";
				break;
			case 308:
				responseCodeText = "Permanent Redirect";
				break;
			case 400:
				responseCodeText = "Bad Request";
				break;
			case 401:
				responseCodeText = "Unauthorized";
				break;
			case 402:
				responseCodeText = "Payment Required";
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
			case 406:
				responseCodeText = "Not Acceptable";
				break;
			case 407:
				responseCodeText = "Proxy Authentication Required";
				break;
			case 408:
				responseCodeText = "Request Timeout";
				break;
			case 409:
				responseCodeText = "Conflict";
				break;
			case 410:
				responseCodeText = "Gone";
				break;
			case 411:
				responseCodeText = "Length Required";
				break;
			case 412:
				responseCodeText = "Precondition Failed";
				break;
			case 413:
				responseCodeText = "Payload Too Large";
				break;
			case 414:
				responseCodeText = "URI Too Long";
				break;
			case 415:
				responseCodeText = "Unsupported Media Type";
				break;
			case 416:
				responseCodeText = "Range Not Satisfiable";
				break;
			case 417:
				responseCodeText = "Expectation Failed";
				break;
			case 418:
				responseCodeText = "I'm a teapot";
				break;
			case 421:
				responseCodeText = "Misdirected Request";
				break;
			case 422:
				responseCodeText = "Unprocessable Entity";
				break;
			case 423:
				responseCodeText = "Locked";
				break;
			case 424:
				responseCodeText = "Failed Dependency";
				break;
			case 425:
				responseCodeText = "Too Early";
				break;
			case 426:
				responseCodeText = "Upgrade Required";
				break;
			case 428:
				responseCodeText = "Precondition Required";
				break;
			case 429:
				responseCodeText = "Too Many Requests";
				break;
			case 431:
				responseCodeText = "Request Header Fields Too Large";
				break;
			case 451:
				responseCodeText = "Unavailable For Legal Reasons";
				break;
			case 500:
				responseCodeText = "Internal Server Error";
				break;
			case 501:
				responseCodeText = "Not Implemented";
				break;
			case 502:
				responseCodeText = "Bad Gateway";
				break;
			case 503:
				responseCodeText = "Service Unavailable";
				break;
			case 504:
				responseCodeText = "Gateway Timeout";
				break;
			case 505:
				responseCodeText = "HTTP Version Not Supported";
				break;
			case 506:
				responseCodeText = "Variant Also Negotiates";
				break;
			case 507:
				responseCodeText = "Insufficient Storage";
				break;
			case 508:
				responseCodeText = "Loop Detected";
				break;
			case 510:
				responseCodeText = "Not Extended";
				break;
			case 511:
				responseCodeText = "Network Authentication Required";
				break;
			default:
				responseCodeText = "Unknown code";
		}

		headersMap.put("Date", (new Date()).toString());
		headersMap.put("Server", "Nebula");
		headersMap.put("Connection", "keep-alive");
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
		if(response.getBody()!=null)
		{
			outputStream.write(response.getBody());
		}
		outputStream.write(-1);

		if(this.closeConnection)
		{
			stop();
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
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}