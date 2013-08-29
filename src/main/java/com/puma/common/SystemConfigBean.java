package com.puma.common;

public class SystemConfigBean{
	
	private static String serverUrl = "";
	private static String ebookTemplatePath = "";
	private static String sdkHome = "";
	private static String jdkHome = "";
	
	public void start(){
		
	}
	
	public void stop(){
		
	}

	public static String getServerUrl() {
		return serverUrl;
	}

	public static void setServerUrl(String serverUrl) {
		SystemConfigBean.serverUrl = serverUrl;
	}

	public static String getEbookTemplatePath() {
		return ebookTemplatePath;
	}

	public static void setEbookTemplatePath(String ebookTemplatePath) {
		SystemConfigBean.ebookTemplatePath = ebookTemplatePath;
	}

	public static String getSdkHome() {
		return sdkHome;
	}

	public static void setSdkHome(String sdkHome) {
		SystemConfigBean.sdkHome = sdkHome;
	}

	public static String getJdkHome() {
		return jdkHome;
	}

	public static void setJdkHome(String jdkHome) {
		SystemConfigBean.jdkHome = jdkHome;
	}
	

}
