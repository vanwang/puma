package com.puma.core.controller;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.puma.core.base.BaseController;

@Controller
public class WelcomeController extends BaseController{
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@RequestMapping("/welcome.html")
	public ModelAndView login(@RequestParam(value="status", required=true, defaultValue="0") int status){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("welcome");
		mv.addObject("serverInfo", getServerInfo());
		return mv;
	}
	
	private Map<String,String> getServerInfo(){
		Map<String, String> serverInfoMap = new HashMap<String, String>();
		
		Properties props=System.getProperties(); //获得系统属性集  
		String osName = props.getProperty("os.name"); //操作系统名称  
		serverInfoMap.put("osName", osName);
		String osArch = props.getProperty("os.arch"); //操作系统构架
		serverInfoMap.put("osArch", osArch);
		//String osVersion = props.getProperty("os.version"); //操作系统版本
		String javaVersion = props.getProperty("java.version"); //Java 运行时环境版本 
		serverInfoMap.put("javaVersion", javaVersion);
		String webApp = props.getProperty("webapp.root"); //Java 运行时环境版本 
		serverInfoMap.put("webApp", webApp);
		int seperatorIndex = webApp.indexOf(File.separator);
		String webappDisk = webApp.substring(0,seperatorIndex)+File.separator;
		
		MemoryMXBean osmb = (MemoryMXBean) ManagementFactory.getMemoryMXBean();
	    String jvmMemoryTotal = osmb.getHeapMemoryUsage().getMax() / 1024 / 1024 + "MB";
	    serverInfoMap.put("jvmMemoryTotal", jvmMemoryTotal);
	    String jvmMemoryUsed =  osmb.getHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB";
	    serverInfoMap.put("jvmMemoryUsed", jvmMemoryUsed);
	    
		
		File[] roots = File.listRoots();//获取磁盘分区列表
        for (File file : roots) {
        	if(webappDisk.equalsIgnoreCase(file.getPath())){
                String diskFree = file.getFreeSpace()/1024/1024/1024+"G";//空闲空间
                String diskTotal = file.getTotalSpace()/1024/1024/1024+"G";//总空间
                serverInfoMap.put("diskTotal", diskTotal);
                serverInfoMap.put("diskFree", diskFree);
        	}
        }
        
        int userNumbers = sessionRegistry.getAllPrincipals().size();
        serverInfoMap.put("userNumbers", Integer.toString(userNumbers));
        
		return serverInfoMap;
	}
}
