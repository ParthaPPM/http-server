package com.ppm.server.http;

public class Authorizer
{
	Authorizer()
	{}

	boolean isAuthorized(Request request)
	{
		String authorizationString = request.getHeaders().get("Authorization");
		if(authorizationString!=null)
		{
			String token = authorizationString.substring(authorizationString.indexOf(" ")+1);
			return true;
		}
		return false;
	}
}