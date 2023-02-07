import io.github.parthappm.http.server.HttpServer;
import io.github.parthappm.http.server.HttpsServer;
import io.github.parthappm.http.server.RequestProcessor;
import io.github.parthappm.http.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		RequestProcessor requestProcessor1 = new MyRequestProcessor();
		RequestProcessor requestProcessor2 = new RequestProcessor();

		Server httpServer = new HttpServer().setRequestProcessor(requestProcessor2);
		Server httpsServer = new HttpsServer()
				.setKeyStore("", "")
				.setRequestProcessor(requestProcessor1)
				.setTimeout(Duration.ofSeconds(30));

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