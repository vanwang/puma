package com.puma.plugin.qrcode.impl;

import org.springframework.stereotype.Component;

import com.puma.core.domain.Member;
import com.puma.core.plugin.IPumaPlugin;

@Component
public class QrCodePluginImpl implements IPumaPlugin{

	@Override
	public String getName() {
		return "二维码转换";
	}
	
	@Override
	public String getJsModuleName() {
		return "QrCodeModule";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
	public void setUp() {
		
	}

	@Override
	public void tearDown() {
	}

	@Override
	public void enable() {
		
	}

	@Override
	public void disable() {
		
	}
	
	@Override
	public String getAuthUrl() {
		return "qrcode.html";
	}

	@Override
	public Class<?>[] depend() {
		Class<?>[] clses = new Class[1];
		Class<?> cls = Member.class;
		clses[0] = cls;
		return clses;
	}

}
