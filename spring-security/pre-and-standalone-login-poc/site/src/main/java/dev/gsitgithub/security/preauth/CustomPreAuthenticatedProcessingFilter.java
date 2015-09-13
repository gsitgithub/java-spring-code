package dev.gsitgithub.security.preauth;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

public class CustomPreAuthenticatedProcessingFilter extends RequestHeaderAuthenticationFilter {
	private Logger Log = LoggerFactory.getLogger(getClass()); 
	private String principalRequestHeader = "LAB_SOLUTIONS";
	
    private final boolean HEADER_AUTHENTICATION = false;
    private static final String AUTHENTICATION = "Authentication";
	
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
    	response.addHeader(principalRequestHeader, "myvalues");
    	Log.debug("successfulAuthentication\n");
    	addCookie(request, response, AUTHENTICATION, authResult.getAuthorities().iterator().next().getAuthority());
    	super.successfulAuthentication(request, response, authResult);
    	response.addHeader(AUTHENTICATION, authResult.getAuthorities().iterator().next().getAuthority());
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
    		HttpServletResponse response, AuthenticationException failed) {
    	Log.debug("unsuccessfulAuthentication \n");
		super.unsuccessfulAuthentication(request, response, failed);
    }
    
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		if(HEADER_AUTHENTICATION || request.getParameterMap().size() >= 1 || 
				(request.getAttribute("username")!=null && request.getAttribute("password")!=null ))
			return true;
	    return false;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		String[] credentials = new String[2];
		if(HEADER_AUTHENTICATION) {
		    credentials[0] = request.getHeader("username");
		    credentials[1] = request.getHeader("password");
		} else if((request.getAttribute("username")!=null && request.getAttribute("password")!=null )) {
		    credentials[0] = (String) request.getAttribute("username");
		    credentials[1] = (String) request.getAttribute("password");
		} else {
			credentials[0] = request.getParameter("username");
			credentials[1] = request.getParameter("password");
		}
	    return credentials;
	}
	
	private void addCookie(HttpServletRequest request, HttpServletResponse response, String pCookieName, String pCookieValue) {
    	if(!checkForCookie(request, pCookieName)){
    		try {
                Cookie cookie = new Cookie(pCookieName, pCookieValue);
                URL url = new URL(request.getRequestURL().toString());
                cookie.setDomain(url.getHost());
                cookie.setPath("/");
                cookie.setComment("user is not eligible to take the survey this time");
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
                Log.info("addCookie \n");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
    	} else
    		for (Cookie cookie : request.getCookies())
				if (cookie.getName().equalsIgnoreCase(pCookieName)) {
					cookie.setValue(pCookieValue);
					response.addCookie(cookie);
					break;
				}
    }
	private boolean checkForCookie(HttpServletRequest pHttpRequest, String pCookieName) {
		if(pHttpRequest.getCookies() != null)
			for (Cookie cookie : pHttpRequest.getCookies())
				if (cookie.getName().equalsIgnoreCase(pCookieName))
					return true;
		return false;
	}
}
