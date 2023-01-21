import io.github.parthappm.http.server.HttpServer;
import io.github.parthappm.http.server.HttpsServer;
import io.github.parthappm.http.server.RequestProcessor;
import io.github.parthappm.http.server.Server;

import java.io.IOException;
import java.time.Duration;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		RequestProcessor requestProcessor = new RequestProcessor("html1").setRootDirectory("html2");

		Server httpServer = new HttpServer().setRequestProcessor(requestProcessor);
		Server httpsServer = new HttpsServer()
				.setKeyStore("", "")
				.setRequestProcessor(requestProcessor)
				.setTimeout(Duration.ofSeconds(30));
		httpServer.start();
	}
}