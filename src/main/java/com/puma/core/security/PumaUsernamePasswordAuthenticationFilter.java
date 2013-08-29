package com.puma.core.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;

import com.puma.core.security.captcha.CaptchaUtil;

/**
 * 实现登录验证
 * @author wangpeng1
 *
 */
public class PumaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		if (!CaptchaUtil.validateCaptchaByRequest(request)) {
			throw new AuthenticationServiceException("captcha error");
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

		// 验证用户账号与密码是否对应
        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        
        // Place the last username attempted into HttpSession for views
        HttpSession session = request.getSession(false);
        
        if (session != null || getAllowSessionCreation()) {
            request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
        }
        
        // 允许子类设置详细属性
        setDetails(request, authRequest);
        
        return this.getAuthenticationManager().authenticate(authRequest);
	}

	/*@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
		return null == obj ? "" : obj.toString();
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
		return null == obj ? "" : obj.toString();
	}*/
}