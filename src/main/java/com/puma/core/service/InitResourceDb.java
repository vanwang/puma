package com.puma.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.puma.core.dao.ResourceDao;
import com.puma.core.domain.Resource;


public class InitResourceDb{
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;
	
	
	public void start() throws ClassNotFoundException{
		List<Resource> allResources = resourceDao.findAll();
		int size = allResources.size();
		Set<RequestMappingInfo> rmSet = handlerMapping.getHandlerMethods().keySet();
		List<Resource> newResources = new ArrayList<Resource>();
		for (RequestMappingInfo rm : rmSet) {
			String url = rm.getPatternsCondition().toString();
			//since url is "[/main.html]",need to remove the braket
			url = url.substring(1,url.length()-1);
			if(url.indexOf("//") != -1){
				url.replace("//", "/");
			}
			if(size == 0){
				Resource rec = new Resource();
				rec.setName("unnamed resource");
				rec.setResourceString(url);
				//rec.setSys(false);
				//rec.setActive(true);
				newResources.add(rec);
			}else{
				boolean has = false;
				for(Resource r : allResources){
					if(r.getResourceString().equalsIgnoreCase(url)){
						has = true;
						break;
					}
				}
				if(!has){
					Resource rec = new Resource();
					rec.setName("unamed resource");
					rec.setResourceString(url);
					//rec.setSys(false);
					//rec.setActive(false);
					newResources.add(rec);
				}
			}
		}
		if(newResources.size() > 0){
			resourceDao.save(newResources);
		}
		
	}
	
	public void stop(){
		
	}



}
