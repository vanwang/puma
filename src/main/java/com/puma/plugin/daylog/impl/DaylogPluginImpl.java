package com.puma.plugin.daylog.impl;

import org.springframework.stereotype.Component;

import com.puma.core.domain.Member;
import com.puma.core.plugin.IPumaPlugin;

@Component
public class DaylogPluginImpl implements IPumaPlugin{

	@Override
	public String getName() {
		return "Daylog日程";
	}
	
	@Override
	public String getJsModuleName() {
		return "DaylogModule";
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
		return "daylog.html";
	}

	@Override
	public Class<?>[] depend() {
		Class<?>[] clses = new Class[1];
		Class<?> cls = Member.class;
		clses[0] = cls;
		return clses;
	}

}
