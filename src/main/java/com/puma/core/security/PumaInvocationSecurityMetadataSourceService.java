package com.puma.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;
import org.springframework.stereotype.Service;

import com.puma.core.dao.ResourceDao;
import com.puma.core.domain.Resource;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。 此类在初始化时，应该取到所有资源及其对应角色的定义。
 * 
 */
@Service
@Singleton
public class PumaInvocationSecurityMetadataSourceService implements
		FilterInvocationSecurityMetadataSource {

	@Autowired
	private ResourceDao resourceDao;
	
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();

	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public PumaInvocationSecurityMetadataSourceService() {
	}
	
	public void start() throws Exception {
		loadResourceDefine();
	}  
	
	public void end() throws Exception {}  
	
	public static void reloadResourceDefine(){
		
	}
	
	public void loadResourceDefine() {
		
		// 在Web服务器启动时，提取系统中的所有权限。
		List<Resource> query = resourceDao.findAll();
		PumaInvocationSecurityMetadataSourceService.loadResourceDefindMethod(query);
	}
	
	public static void loadResourceDefindMethod(List<Resource> query){
		/*
		 * 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
		 * sparta
		 */
		HashMap<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

		for (Resource resource : query) {
				
				ConfigAttribute ca = new SecurityConfig(resource.getAuthority());
			
				String url = resource.getResourceString();
				
				/*
				 * 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
				 * sparta
				 */
				if (resourceMap.containsKey(url)) {

					Collection<ConfigAttribute> value = resourceMap.get(url);
					value.add(ca);
					resourceMap.put(url, value);
				} else {
					Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
					atts.add(ca);
					resourceMap.put(url, atts);
				}

		}
		
		PumaInvocationSecurityMetadataSourceService.resourceMap = resourceMap;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {

		return null;
	}

	// 根据URL，找到相关的权限配置。
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {

		// object 是一个URL，被用户请求的url。
		String url = ((FilterInvocation) object).getRequestUrl();
		
        int firstQuestionMarkIndex = url.indexOf("?");

        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }

		Iterator<String> ite = resourceMap.keySet().iterator();

		while (ite.hasNext()) {
			String resURL = ite.next();
			if (urlMatcher.pathMatchesUrl(url, resURL)) {

				return resourceMap.get(resURL);
			}
		}
		
		//由于只对添加在数据库中的资源权限做管理，如果返回null，将表示数据库中不存在的请求资源将不会被验证，accessDecision才有用。为了避免资源项目的输入遗漏，添加下面的语句
		//新增的效是：如果数据库中有权限，则调用accessDecision去判断用户是否有权限；如果数据库中没有包含所请求的路径，则表明不允许访问。
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		ConfigAttribute ca = new SecurityConfig("UN_AUTHORIZED");
		atts.add(ca);
		return atts;
		//如果想要的效果是只管理数据库中的资源，其他资源不受访问限制，则返回null
		//return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {

		return true;
	}

}