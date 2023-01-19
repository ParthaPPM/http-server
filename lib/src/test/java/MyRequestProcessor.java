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
			//return new Response("Hello").addHeader("Greeting", "Hello");
			return fromFile("LICENSE");
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

	@Override
	public Response head(Request request)
	{
		return super.head(request);
	}

	@Override
	public Response post(Request request)
	{
		return super.post(request);
	}

	@Override
	public Response put(Request request)
	{
		return super.put(request);
	}

	@Override
	public Response delete(Request request)
	{
		return super.delete(request);
	}

	@Override
	public Response connect(Request request)
	{
		return super.connect(request);
	}

	@Override
	public Response options(Request request)
	{
		return super.options(request);
	}

	@Override
	public Response trace(Request request)
	{
		return super.trace(request);
	}

	@Override
	public Response patch(Request request)
	{
		return super.patch(request);
	}

	@Override
	public Response none(Request request)
	{
		return super.none(request);
	}
}
