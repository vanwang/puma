package com.puma.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtils
{

    private WebUtils()
    {
    }
    
    /**
     * 判断是否为Ajax请求
     * @param request   HttpServletRequest
     * @return  是true, 否false
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否非静态资源
     * @param request   HttpServletRequest
     * @return  是true, 否false
     *//*
    public static boolean isValidRequestType(HttpServletRequest request) {
    	String fileType[] = {".html",".html",".do",".json"};
    	String uri = request.getRequestURI();
        for(String type : fileType){
        	if(uri.indexOf(type) != -1){
        		return true;
        	}
        }
        return false;
    }*/

}