package com.puma.core.plugin.impl;

import org.springframework.stereotype.Component;

import com.puma.core.domain.Member;
import com.puma.core.plugin.IPumaPlugin;

@Component
public class RoleManagementPluginImpl implements IPumaPlugin{

	@Override
	public String getName() {
		return "角色管理";
	}
	
	@Override
	public String getJsModuleName() {
		return "RoleManagementModule";
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
		return "rolemanagement.html";
	}
	
	@Override
	public Class<?>[] depend() {
		Class<?>[] clses = new Class[1];
		Class<?> cls = Member.class;
		clses[0] = cls;
		return clses;
	}

}
