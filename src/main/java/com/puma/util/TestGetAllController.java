package com.puma.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.puma.core.dao.ResourceDao;
import com.puma.core.domain.Resource;
import com.puma.core.plugin.IPumaPlugin;

public class TestGetAllController{
	
	@Autowired
	private ResourceDao resourceDao;
	
	@SuppressWarnings("unused")
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;
	
	
	public void start() throws ClassNotFoundException{
		/*Set<RequestMappingInfo> rmSet = handlerMapping.getHandlerMethods().keySet();
		for (RequestMappingInfo rm : rmSet) {
			System.out.println(rm.getPatternsCondition().toString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
		    if("[YourURLPath]".equals(rm.getPatternsCondition().toString())) {
		        // URL mapping matched 
		    }
		}*/
		
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        for (BeanDefinition beanDefinition : scanner.findCandidateComponents("com.puma.**.plugin.**")){
        	//AnnotationUtils.findAnnotation(Class.forName(beanDefinition.getBeanClassName()), ResponseBody.class);
        	System.out.println("+++++++++++++++++++++++++++++++++++++++");
            //System.out.println(beanDefinition.getBeanClassName());
        	boolean isImplementor = ClassUtils.isInterface(Class.forName(beanDefinition.getBeanClassName()), "com.puma.core.plugin.IPumaPlugin");
        	if(isImplementor){
        		List<Resource> resources = resourceDao.findAll();
        		for(Resource r : resources){
        			System.out.println(r.getResourceString());
        		}
        		System.out.println(beanDefinition.getBeanClassName());
        		IPumaPlugin o = (IPumaPlugin) SpringUtils.getApplicationContext().getBean(Class.forName(beanDefinition.getBeanClassName()));
        		//IPumaPlugin o = (IPumaPlugin) SpringUtils.getBean(beanDefinition.getBeanClassName());
        		System.out.println(o.getName());
        	}
            System.out.println("+++++++++++++++++++++++++++++++++++++++");
        }
		/*ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
	    scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));

	    for (String componentBasePacke : componentBasePackages) {
	        for (BeanDefinition bd : scanner.findCandidateComponents(componentBasePacke)) {
	            try {
	                components.add(Class.forName(bd.getBeanClassName()));
	            } catch (ClassNotFoundException ex) {
	            }
	        }
	    }*/
		
	}
	
	public void stop(){
		
	}



}
