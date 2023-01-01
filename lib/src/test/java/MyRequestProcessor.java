import io.github.parthappm.http.server.Request;
import io.github.parthappm.http.server.RequestProcessor;
import io.github.parthappm.http.server.Response;

import java.util.HashMap;
import java.util.Map;

public class MyRequestProcessor extends RequestProcessor
{
	public Response get(Request request)
	{
		if (request.path().equals("/"))
		{
			return new Response("Hello").addHeader("Greeting", "Hello");
		}
		else if (request.path().equals("/partha"))
		{
			Map<String, String> headers = new HashMap<>();
			headers.put("Status", "Not found");
			return new Response().setHeader(headers);
		}
		else
		{
			return new Response().setBody("Page not found");
		}
	}
}
