package com.puma.core.security.captcha;

import javax.servlet.http.HttpServletRequest;

import com.puma.core.security.captcha.CaptchaEngine;
import com.puma.util.SpringUtils;

import org.apache.commons.lang.StringUtils;

import com.octo.captcha.service.CaptchaService;

public class CaptchaUtil {
	
	private static final String CAPTCHA_SERVICE_BEAN_NAME = "captchaService";// CaptchaService Bean名称
	
	/**
	 * 根据HttpServletRequest校验验证码
	 * 
	 * @param parameterName
	 *         参数名称
	 * 
	 * @return 是否验证通过
	 */
	public static boolean validateCaptchaByRequest(HttpServletRequest request, String parameterName) {
		String captchaID = request.getSession().getId();
		String challengeResponse = StringUtils.upperCase(request.getParameter(parameterName));
		
		if (StringUtils.isEmpty(captchaID) || StringUtils.isEmpty(challengeResponse)) {
			return false;
		}
		
		CaptchaService captchaService = (CaptchaService) SpringUtils.getBean(CAPTCHA_SERVICE_BEAN_NAME);
		try {
			if (captchaService.validateResponseForID(captchaID, challengeResponse)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据HttpServletRequest校验验证码，使用默认参数名称
	 * 
	 * @return 是否验证通过
	 */
	public static boolean validateCaptchaByRequest(HttpServletRequest request) {
		return validateCaptchaByRequest(request, CaptchaEngine.CAPTCHA_PARAMETER_NAME);
	}

}