package com.puma.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.puma.util.WebUtils;

public class PumaSessionManagementFilter extends GenericFilterBean {
    //~ Static fields/initializers =====================================================================================

    static final String FILTER_APPLIED = "__spring_security_session_mgmt_filter_applied";

    private final SecurityContextRepository securityContextRepository;
    private SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private String invalidSessionUrl;
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public PumaSessionManagementFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
            return;
        }

        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
        if (!securityContextRepository.containsContext(request)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && !authenticationTrustResolver.isAnonymous(authentication)) {
             // The user has been authenticated during the current request, so call the session strategy
                try {
                    sessionStrategy.onAuthentication(authentication, request, response);
                } catch (SessionAuthenticationException e) {
                    // The session strategy can reject the authentication
                    logger.debug("SessionAuthenticationStrategy rejected the authentication object", e);
                    SecurityContextHolder.clearContext();
                    failureHandler.onAuthenticationFailure(request, response, e);

                    return;
                }
                // Eagerly save the security context to make it available for any possible re-entrant
                // requests which may occur before the current request completes. SEC-1396.
                securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            } else {
             // No security context or authentication present. Check for a session timeout
                if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
                    logger.debug("Requested session ID" + request.getRequestedSessionId() + " is invalid.");
                    
                    //wangfan only change the original code below
                    	if(WebUtils.isAjaxRequest(request)){
                  		   
                  		   response.setContentType("text/html;charset=UTF-8");
                      	   response.setStatus(PumaResponseStatus.PUMA_SC_SESSION_TIMEOUT);
                      	  return;
                  	   }else{
                  		   if (invalidSessionUrl != null) {
                  			    logger.debug("Starting new session (if required) and redirecting to '" + invalidSessionUrl + "'");
                  			    request.getSession();
                                 redirectStrategy.sendRedirect(request, response, invalidSessionUrl);
                                 return;
                             }
                  	   }
                    /*if (invalidSessionUrl != null) {
                        logger.debug("Starting new session (if required) and redirecting to '" + invalidSessionUrl + "'");
                        request.getSession();
                        redirectStrategy.sendRedirect(request, response, invalidSessionUrl);

                        return;
                    }*/
                }
            }
        }

        chain.doFilter(request, response);
    }

    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {
        Assert.notNull(sessionStrategy, "authenticatedSessionStratedy must not be null");
        this.sessionStrategy = sessionStrategy;
    }

    public void setInvalidSessionUrl(String invalidSessionUrl) {
        this.invalidSessionUrl = invalidSessionUrl;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null");
        this.failureHandler = failureHandler;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy  = redirectStrategy;
    }
}
