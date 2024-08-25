import io.github.parthappm.http.server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		ServerProperties serverProperties = new ServerProperties();
		serverProperties.setPort(8080);
		serverProperties.setHost("127.0.0.1");
		serverProperties.setKeyStoreFileName("file name");
		serverProperties.setKeyStorePassword("password");
		serverProperties.setServerTimeoutInMillis(10000);
		serverProperties.setServerName("Test Server");
		serverProperties.setRootDirectory("");
		serverProperties.setLogFileName("log_file.txt");
		serverProperties.setLogDateFormat("[HH:mm:ss]");

		Server httpServer = new HttpServer(serverProperties);
		httpServer.addController("get", "/index.html", request -> new Response().setBody("This is index page"));
		httpServer.addController("GET", "/test", new TestController());
		Server httpsServer = new HttpsServer(serverProperties);
		httpsServer.addController("get", "/index.html", request -> new Response().setBody("This is also index page"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean continueExecution = true;
		System.out.println("Enter choice");
		do
		{
			switch (br.readLine())
			{
				case "start http" -> httpServer.start();
				case "start https" -> httpsServer.start();
				case "stop http" -> httpServer.stop();
				case "stop https" -> httpsServer.stop();
				case "stop" ->
				{
					httpServer.stop();
					httpsServer.stop();
					continueExecution = false;
				}
			}
		} while (continueExecution);
	}
}