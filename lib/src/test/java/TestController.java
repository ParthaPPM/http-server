import io.github.parthappm.http.server.Controller;
import io.github.parthappm.http.server.Request;
import io.github.parthappm.http.server.Response;

public class TestController implements Controller
{
	public TestController()
	{}

	public Response process(Request request)
	{
		String serveFrom = request.getParameters().get("from");
		if (serveFrom.equals("file"))
		{
			return null; //this will return the file present in the root folder if present else 404 error
		}
		else
		{
			return new Response().setBody("This is test page");
		}
	}
}