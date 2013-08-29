package com.puma.core.plugin;

public interface IPumaPlugin {

		String getName();
		
		String getJsModuleName();
		
		String getVersion();
		
		String getAuthUrl();
		
		void setUp();
		
		void tearDown();
		
		void enable();
		
		void disable();
		
		Class<?>[] depend();
}
