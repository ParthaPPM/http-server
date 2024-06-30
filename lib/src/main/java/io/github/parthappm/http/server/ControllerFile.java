package io.github.parthappm.http.server;

class ControllerFile implements Controller
{
	ControllerFile(ServerProperties properties)
	{}

	public Response process(Request request)
	{
		return new Response(404);
	}
}
