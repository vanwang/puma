package com.puma.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puma.common.BusinessHandler;
import com.puma.common.ResponseMessage;
import com.puma.core.base.BaseController;
import com.puma.core.domain.Plugin;
import com.puma.core.service.PluginService;
import com.puma.exception.BusinessException;

@Controller
@RequestMapping("/plugin")
public class PluginController extends BaseController{
	
	@Autowired
	private PluginService pluginService;
	
	@RequestMapping("/unrelatedplugintree.json")
	public @ResponseBody List<Map<String,Object>> getPluginTreeDate(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = pluginService.getUnrelatedPluginTreeData();
		} catch (Exception e) {
		}
		return list;
	}
	
	@RequestMapping("/plugincombo.json")
	public @ResponseBody List<Map<String,Object>> getPluginComboDate(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = pluginService.getPluginComboData();
		} catch (Exception e) {
		}
		return list;
	}
	
	@RequestMapping("/getallplugininfo.json")
	public @ResponseBody List<Plugin> getAllPluginInfo(){
			return pluginService.findAll();
	}
	
	@RequestMapping("/savepluginrelation.do")
	public @ResponseBody ResponseMessage savePluginRelation(@RequestParam(value = "pluginid") final String pluginId
															,@RequestParam(value = "treeid") final String menuId){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Plugin plugin = pluginService.findById(pluginId);
				plugin.setMenuId(menuId);
				pluginService.save(plugin);
				return plugin.getId();
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/removepluginrelations.do")
	public @ResponseBody ResponseMessage removePluginRelations(@RequestParam(value = "ids[]",required=false) final String[] pluginIds){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				if(pluginIds == null){
					throw new BusinessException("no relations to remove!");
				}
				
				List<Plugin> plugins = new ArrayList<Plugin>();
				
				for(String pluginId:pluginIds){
					Plugin plugin = pluginService.findById(pluginId);
					if(plugin == null){
						throw new BusinessException("plugin not found!");
					}
					plugin.setMenuId(null);
					plugins.add(plugin);
				}
				
				if(plugins.size() > 0){
					pluginService.save(plugins);
				}
				return "success";
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("/removepluginrelation.do")
	public @ResponseBody ResponseMessage removePluginRelation(@RequestParam(value = "id",required=false) final String pluginId){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				if(pluginId == null){
					throw new BusinessException("plugin not found!");
				}
				Plugin plugin = pluginService.findById(pluginId);
				if(plugin == null){
					throw new BusinessException("plugin not found!");
				}
				plugin.setMenuId(null);
				pluginService.save(plugin);
				return plugin.getId();
			}
		}, logger);
		return response;
	}
	
}
