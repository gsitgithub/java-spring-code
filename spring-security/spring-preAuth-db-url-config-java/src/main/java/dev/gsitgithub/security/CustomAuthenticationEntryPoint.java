package dev.gsitgithub.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException arg2) throws IOException, ServletException {
		System.out.println("\nCustomAuthenticationEntryPoint\n"+request.getAttributeNames()+"--\n");
//		response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
		response.sendRedirect("/accessdenied");
	}

}
