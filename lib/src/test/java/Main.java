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
		RequestProcessor requestProcessor = new RequestProcessor().setRootDirectory("");
		Server httpServer = new HttpServer().setRequestProcessor(requestProcessor);
		Server httpsServer = new HttpsServer()
				.setKeyStore("", "")
				.setRequestProcessor(new MyRequestProcessor())
				.setTimeout(Duration.ofSeconds(20));
		httpServer.start();
	}
}