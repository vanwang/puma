package com.puma.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.puma.core.domain.Plugin;
import com.puma.core.plugin.IPumaPlugin;
import com.puma.util.ClassUtils;
import com.puma.util.SpringUtils;

public class InitSystemPluginDb{
	
	@Autowired
	private PluginService pluginService;
	
	
	public void start() throws ClassNotFoundException{
		
		List<Plugin> plugins = pluginService.findAll();
		//int size = plugins.size();
		List<Plugin> newPlugins = new ArrayList<Plugin>();
		
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        for (BeanDefinition beanDefinition : scanner.findCandidateComponents("com.puma.**.plugin.**")){
        	boolean isImplementor = ClassUtils.isInterface(Class.forName(beanDefinition.getBeanClassName()), "com.puma.core.plugin.IPumaPlugin");
        	if(isImplementor){
        		IPumaPlugin o = (IPumaPlugin) SpringUtils.getApplicationContext().getBean(Class.forName(beanDefinition.getBeanClassName()));
        		Assert.notNull(o.getJsModuleName(), beanDefinition.getBeanClassName()+"do not have a valid JS module name, you must set a valid one!");
        		String jsModuleName = o.getJsModuleName();
        		/*if(size == 0){
        			Plugin newp = new Plugin();
        			newp.setJsModuleName(o.getJsModuleName());
        			newp.setName((o.getName()==null&&o.getName().trim().length()==0)?"unnamed":o.getName());
        			newp.setVersion(o.getVersion()==null?"0":o.getVersion());
        			newp.setAuthUrl(o.getAuthUrl());
        			newPlugins.add(newp);
        		}else{*/
        			boolean has = false;
        			for(Plugin p:plugins){
            			if(p.getJsModuleName().equalsIgnoreCase(jsModuleName)){
            				has = true;
            				break;
            			}
            		}
            		if(!has){
            			Plugin newp = new Plugin();
            			newp.setJsModuleName(o.getJsModuleName());
            			newp.setName((o.getName()==null&&o.getName().trim().length()==0)?"unnamed":o.getName());
            			newp.setVersion(o.getVersion()==null?"0":o.getVersion());
            			newp.setAuthUrl(o.getAuthUrl());
            			newPlugins.add(newp);
            		}
        		//}
        	}
        }
        if(newPlugins.size() > 0){
        	pluginService.save(newPlugins);
		}
		
	}
	
	public void stop(){
		
	}



}
