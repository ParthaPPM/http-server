import io.github.parthappm.http.server.HttpServer;
import io.github.parthappm.http.server.HttpsServer;
import io.github.parthappm.http.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Server http = new HttpServer().setRequestProcessor(new MyRequestProcessor());
		Server https = new HttpsServer()
				.setKeyStore("", "")
				.setRequestProcessor(new MyRequestProcessor())
				.setTimeout(Duration.ofSeconds(20));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			switch (br.readLine())
			{
				case "start http" -> http.start();
				case "start https" -> https.start();
				case "stop http" -> http.stop();
				case "stop https" -> https.stop();
				case "stop" ->
				{
					http.stop();
					https.stop();
				}
			}
		}
	}
}