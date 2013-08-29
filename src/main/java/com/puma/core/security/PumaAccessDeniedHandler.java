package com.puma.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;

import com.puma.util.WebUtils;

public class PumaAccessDeniedHandler implements AccessDeniedHandler {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String accessDeniedUrl;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {

		if(WebUtils.isAjaxRequest(request)){
   		   response.setContentType("text/html;charset=UTF-8");
       	   response.setStatus(HttpServletResponse.SC_FORBIDDEN);
       	   return;
   	   	}else{
   		   if (accessDeniedUrl != null) {
   			    logger.debug("you do not have access to to '" + request.getRequestURI() + "'");
   			    request.getSession();
                  redirectStrategy.sendRedirect(request, response, accessDeniedUrl);
                  return;
              }
   	   	}

	}

	public String getAccessDeniedUrl() {
		return accessDeniedUrl;
	}

	public void setAccessDeniedUrl(String accessDeniedUrl) {
		Assert.hasText(accessDeniedUrl, "Need to defind an accessDeniedUrl!");
		this.accessDeniedUrl = accessDeniedUrl;
	}
	
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
	    this.redirectStrategy = redirectStrategy;
	}
}