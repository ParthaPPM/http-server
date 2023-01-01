package io.github.parthappm.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server
{
	private final String NAME;
	private final String VERSION;
	private ServerSocket serverSocket;
	private RequestProcessor requestProcessor;
	private int timeoutInMilliSeconds;

	Server()
	{
		this.NAME = "Nebula";
		this.VERSION = "HTTP/1.1";
		this.requestProcessor = new RequestProcessor();
		this.timeoutInMilliSeconds = 30000;
	}

	void setServerSocket(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
	}

	public Server setRequestProcessor(RequestProcessor requestProcessor)
	{
		this.requestProcessor = requestProcessor;
		return this;
	}

	public Server setTimeout(Duration duration)
	{
		this.timeoutInMilliSeconds = (int) duration.toMillis();
		return this;
	}

	public void start()
	{
		new Thread(() -> {
			try
			{
				do
				{
					Socket socket = serverSocket.accept();
					new Thread(() -> handleRequest(socket)).start();
				} while (serverSocket != null);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				stop();
			}
		}).start();
		System.out.println("Server started...");
	}

	private void handleRequest(Socket socket)
	{
		try
		{
			// setting some string values
			String LINE_SEPARATOR = "\r\n";
			String CONTENT_LENGTH = "Content-Length";
			String CONNECTION = "Connection";
			String CONNECTION_CLOSE = "close";

			socket.setSoTimeout(timeoutInMilliSeconds);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			// keeping the connection open until the connection is closed by the client
			do
			{
				// reading the request line
				String method = readInputStream(is, ' ').toUpperCase();
				String url = readInputStream(is, ' ');
				readInputStream(is, '\n'); // reading the HTTP version

				// reading the request headers
				Map<String, String> requestHeaders = new HashMap<>();
				do
				{
					String header = readInputStream(is, '\n');
					if (header.equals(""))
					{
						break;
					}
					else
					{
						int colonIndex = header.indexOf(':');
						String key = header.substring(0, colonIndex).trim();
						String value = header.substring(colonIndex + 1).trim();
						requestHeaders.put(key, value);
					}
				} while (true);

				// reading the request body
				byte[] requestBody;
				int bytesRead;
				String contentLength = requestHeaders.get(CONTENT_LENGTH);
				if (contentLength != null)
				{
					int requestContentLength = Integer.parseInt(contentLength);
					requestBody = new byte[requestContentLength];
					bytesRead = is.read(requestBody, 0, requestContentLength);
				}
				else
				{
					requestBody = new byte[0];
					bytesRead = 0;
				}

				// extracting the path, request parameters and reference
				String[] parsedUrl = url.split("[?#]");
				String path = parsedUrl.length >= 1 ? URLDecoder.decode(parsedUrl[0], StandardCharsets.UTF_8) : "";
				String parametersString = parsedUrl.length >= 2 && url.contains("?") ? parsedUrl[1] : "";
				String anchor;
				if (parsedUrl.length >= 2)
				{
					if (parsedUrl.length >= 3)
					{
						anchor = URLDecoder.decode(parsedUrl[2], StandardCharsets.UTF_8);
					}
					else
					{
						anchor = url.contains("#") ? URLDecoder.decode(parsedUrl[1], StandardCharsets.UTF_8) : "";
					}
				}
				else
				{
					anchor = "";
				}
				Map<String, String> requestParameters = new HashMap<>();
				if (parametersString.length() != 0)
				{
					for (String parameter : parametersString.split("&"))
					{
						String[] keyValue = parameter.split("=");
						if (keyValue.length >= 2)
						{
							requestParameters.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8), URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
						}
					}
				}

				// logging the request
				Logger.getInstance().log(socket.getInetAddress().getHostAddress() + " " + method + " " + path);

				// generating the response
				Request request = new Request(method, path, requestParameters, anchor, requestHeaders, Arrays.copyOfRange(requestBody, 0, bytesRead));
				Response response;
				try
				{
					response = switch (method)
							{
								case "GET" -> requestProcessor.get(request);
								case "HEAD" -> requestProcessor.head(request);
								case "POST" -> requestProcessor.post(request);
								case "PUT" -> requestProcessor.put(request);
								case "DELETE" -> requestProcessor.delete(request);
								case "CONNECT" -> requestProcessor.connect(request);
								case "OPTIONS" -> requestProcessor.options(request);
								case "TRACE" -> requestProcessor.trace(request);
								case "PATCH" -> requestProcessor.patch(request);
								default -> requestProcessor.none(request);
							};
				} catch (Exception e)
				{
					e.printStackTrace();
					response = new Response().setStatusCode(500);
				}

				// sending the response status
				os.write((VERSION + " " + response.statusCode() + " " + response.statusText() + LINE_SEPARATOR).getBytes(StandardCharsets.UTF_8));

				// sending the headers
				Map<String, String> responseHeaders = response.headers();
				responseHeaders.put("Date", (new Date()).toString());
				responseHeaders.put("Server", NAME);
				responseHeaders.put("Content-Length", String.valueOf(response.body().length));
				for (String key : responseHeaders.keySet())
				{
					if (key != null)
					{
						String value = responseHeaders.get(key);
						if (value != null)
						{
							String line = key + ": " + value + LINE_SEPARATOR;
							os.write(line.getBytes(StandardCharsets.UTF_8));
						}
					}
				}
				os.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));

				// sending the body
				os.write(response.body());

				// checking the condition
				String connection = requestHeaders.get(CONNECTION);
				if (connection != null && connection.equals(CONNECTION_CLOSE))
				{
					break;
				}
			} while (true);
			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				socket.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private String readInputStream(InputStream is, char endChar) throws IOException
	{
		StringBuilder temp = new StringBuilder();
		do
		{
			int b = is.read();
			temp.append((char) b);
			if (b == endChar)
			{
				break;
			}
		} while (true);
		return temp.toString().trim();
	}

	public void stop()
	{
		if (serverSocket != null)
		{
			try
			{
				serverSocket.close();
				System.out.println("Server stopped!");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			serverSocket = null;
		}
	}
}