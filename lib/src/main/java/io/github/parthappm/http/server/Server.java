package io.github.parthappm.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A server class to implement the HTTP server functionality.
 * The object of this class cannot be created directly, either create an object of HttpServer or HttpsServer class.
 */
public class Server
{
	private record ControllerDetails(String method, String path, boolean isRegex, Controller controller)
	{}

	protected final ServerProperties properties;
	private final List<ControllerDetails> controllerList;
	private final Logger logger;
	protected ServerSocket serverSocket;

	Server(ServerProperties properties)
	{
		this.properties = properties;
		this.controllerList = new ArrayList<>();
		this.logger = Logger.getInstance(properties);
	}

	public void addController(String method, String path, Controller controller)
	{
		addController(method, path, false, controller);
	}

	public void addController(String method, String path, boolean isRegex, Controller controller)
	{
		if (controller != null)
		{
			this.controllerList.add(new ControllerDetails(method.toUpperCase(), path, isRegex, controller));
		}
	}

	public void start()
	{
		if (serverSocket != null)
		{
			new Thread(() -> {
				logger.info("Server started at port: " + properties.getPort());
				logger.debug("Host: " + properties.getHost());
				logger.debug("Port: " + properties.getPort());
				logger.debug("Keystore file name: " + properties.getKeyStoreFileName());
				logger.debug("Keystore file password: " + properties.getKeyStorePassword());
				try
				{
					while (true)
					{
						Socket socket = serverSocket.accept();
						new Thread(() -> {
							try
							{
								handleRequest(socket);
							}
							catch (IOException ignore)
							{
								try
								{
									socket.close();
								}
								catch (IOException ignore2) {}
							}
						}).start();
					}
				}
				catch (IOException ignore) {}
				logger.info("Server Stopped!!!");
			}).start();
		}
	}

	/**
	 * Stop the server to listen
	 */
	public void stop()
	{
		if (serverSocket != null)
		{
			try
			{
				this.serverSocket.close();
			}
			catch (IOException ignore) {}
			this.serverSocket = null;
		}
		logger.stop();
	}

	private String readInputStream(InputStream is, char endChar) throws IOException
	{
		StringBuilder temp = new StringBuilder();
		do
		{
			int b = is.read();
			if (b == -1)
			{
				break;
			}
			temp.append((char) b);
			if (b == endChar)
			{
				break;
			}
		} while (true);
		return temp.toString().trim();
	}

	private void readRequestLine(InputStream is, Request request) throws IOException
	{
		request.setMethod(readInputStream(is, ' '));
		Matcher urlMatcher = Pattern.compile("(/[^?#]*)([?]([^#]*))?(#(.*))?").matcher(readInputStream(is, ' '));
		request.setHttpVersion(readInputStream(is, '\n')); // reading and ignoring the HTTP version

		// extracting the path, request parameters and reference
		if (urlMatcher.find())
		{
			request.setPath(urlMatcher.group(1));
			request.setParameters(urlMatcher.group(3));
		}
	}

	private void readRequestHeaders(InputStream is, Request request) throws IOException
	{
		do
		{
			String header = readInputStream(is, '\n');
			if (header.isEmpty())
			{
				break;
			}
			else
			{
				String[] keyValue = header.split(":");
				if (keyValue.length == 1)
				{
					request.addHeader(keyValue[0], "");
				}
				else if (keyValue.length == 2)
				{
					request.addHeader(keyValue[0], keyValue[1].substring(1));
				}
			}
		} while (true);
	}

	private void readRequestBody(InputStream is, Request request) throws IOException
	{
		String contentLength = request.getHeaders().get("Content-Length");
		if (contentLength != null)
		{
			int requestContentLength = Integer.parseInt(contentLength);
			byte[] requestBody = new byte[requestContentLength];
			int totalBytesRead = 0;
			do
			{
				int currentBytesRead = is.read(requestBody, totalBytesRead, requestContentLength);
				totalBytesRead += currentBytesRead;
			} while (totalBytesRead < requestContentLength);
			request.setBody(requestBody);
		}
	}

	private Response generateResponse(Request request)
	{
		// finding the correct controller object
		Controller controller = new ControllerFile(properties);
		for (ControllerDetails controllerDetails : controllerList)
		{
			if (request.getMethod().equals(controllerDetails.method()))
			{
				if (controllerDetails.isRegex() && request.getPath().matches(controllerDetails.path()))
				{
					controller = controllerDetails.controller();
					break;
				}
				else
				{
					String[] actualPaths = request.getPath().substring(1).split("/");
					String[] expectedPaths = controllerDetails.path().substring(1).split("/");
					if (actualPaths.length == expectedPaths.length)
					{
						boolean pathMatched = true;
						for (int i = 0; i < expectedPaths.length; i++)
						{
							if (expectedPaths[i].startsWith("{") && expectedPaths[i].endsWith("}"))
							{
								request.addPathParameters(expectedPaths[i].substring(1, expectedPaths[i].length()-1), actualPaths[i]);
							}
							else
							{
								pathMatched = pathMatched && (expectedPaths[i].equals(actualPaths[i]));
							}
						}
						if (pathMatched)
						{
							controller = controllerDetails.controller();
							break;
						}
					}
				}
			}
		}

		// calling the process method of the controller object
		Response response;
		try
		{
			response = controller.process(request);
			if (response == null)
			{
				response = new Response(404);
			}
		}
		catch (Exception e)
		{
			response = new Response(500);
		}

		return response;
	}

	private void writeResponse(OutputStream os, Response response) throws IOException
	{
		// writing the response status
		os.write((properties.getHttpVersion() + " " + response.statusCode() + " " + response.statusText() + "\r\n").getBytes(StandardCharsets.UTF_8));

		// writing the headers
		os.write(("Date: " + properties.getResponseDateFormat().format(new Date()) + "\r\n").getBytes(StandardCharsets.UTF_8));
		os.write(("Server: " + properties.getServerName() + "\r\n").getBytes(StandardCharsets.UTF_8));
		os.write(("Content-Length: " + response.body().length + "\r\n").getBytes(StandardCharsets.UTF_8));
		for (Map.Entry<String, String> header : response.headers().entrySet())
		{
			String line = header.getKey() + ": " + header.getValue() + "\r\n";
			os.write(line.getBytes(StandardCharsets.UTF_8));
		}
		os.write("\r\n".getBytes(StandardCharsets.UTF_8));

		// writing the body
		os.write(response.body());
	}

	private void handleRequest(Socket socket) throws IOException
	{
		socket.setSoTimeout(properties.getServerTimeoutInMillis());
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();

		// keeping the connection open until the connection is closed by the client
		while (true)
		{
			Request request = new Request(socket.getInetAddress().getHostAddress());
			readRequestLine(is, request);
			readRequestHeaders(is, request);
			readRequestBody(is, request);
			logger.info(request.getIp() + " " + request.getMethod() + " " + request.getPath());
			logger.debug(request.getHeaders());

			writeResponse(os, generateResponse(request));

			// checking the condition
			String connection = request.getHeaders().get("Connection");
			if (connection != null && connection.equals("close"))
			{
				break;
			}
		}
		socket.close();
	}
}