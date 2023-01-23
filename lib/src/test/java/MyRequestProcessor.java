import io.github.parthappm.http.server.Request;
import io.github.parthappm.http.server.RequestProcessor;
import io.github.parthappm.http.server.Response;

public class MyRequestProcessor extends RequestProcessor
{
	public Response get(Request request)
	{
		return fromResource(request.path().substring(1));
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
