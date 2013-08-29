package com.puma.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.PluginDao;
import com.puma.core.domain.Plugin;

@Service("plugin.pluginservice")
@Singleton
public class PluginService extends BaseService<Plugin, String>{

	@SuppressWarnings("unused")
	@Autowired
	private PluginDao pluginDao;
	
	@Autowired
	public void setBaseDao(PluginDao pluginDao) {
		super.setBaseDao(pluginDao);
	}
	
	
	@Transactional
	public List<Map<String,Object>> getAllPluginTreeData()throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		List<Plugin> plugins = this.findAllBySortCreateDate(Direction.DESC);
		for(Plugin  plugin : plugins){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", plugin.getId());
			map.put("iconCls", "icon-none");
			map.put("text", plugin.getName());
			map.put("checked", false);
			map.put("state", "open");
			
			Map<String,Object> attMap=new HashMap<String,Object>();
			attMap.put("hash", "#module="+plugin.getJsModuleName()+"|{}");
			attMap.put("version", plugin.getVersion());
			attMap.put("active", plugin.isActive());
			attMap.put("menuid", plugin.getMenuId());
			attMap.put("authurl", plugin.getAuthUrl());
			if(plugin.getImageClass()!=null && plugin.getImageClass().trim().length()>0){
				attMap.put("image", plugin.getImageClass());
			}
			attMap.put("type", "plugin");
			
			map.put("attributes", attMap);
			
			resultList.add(map);
		}
		
		
		return resultList;
	
	}
	
	@Transactional
	public List<Map<String,Object>> getUnrelatedPluginTreeData()throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> folderMap=new HashMap<String,Object>();
		folderMap.put("iconCls", "icon-none");
		folderMap.put("text", "默认分组");
		folderMap.put("checked", false);
		folderMap.put("state", "open");
		
		Map<String,Object> folderAttMap=new HashMap<String,Object>();
		folderAttMap.put("type", "folder");
		
		folderMap.put("attributes", folderAttMap);
		
		resultList.add(folderMap);
		
		List<Plugin> plugins = this.findAllBySortCreateDate(Direction.DESC);
		for(Plugin  plugin : plugins){
			boolean isRelated = false;
			if(plugin.getMenuId() != null && plugin.getMenuId().trim().length()>0){
				isRelated = true;
			}
			Map<String,Object> map=new HashMap<String,Object>();
			if(isRelated){
				map.put("id", UUID.randomUUID().toString().replace("-", ""));
			}else{
				map.put("id", plugin.getId());
			}
			if(isRelated){
				map.put("iconCls", "icon-customized-module-link");
			}else{
				map.put("iconCls", "icon-none");
			}
			map.put("text", plugin.getName());
			map.put("checked", false);
			map.put("state", "open");
			
			Map<String,Object> attMap=new HashMap<String,Object>();
			attMap.put("hash", "#module="+plugin.getJsModuleName()+"|{}");
			attMap.put("version", plugin.getVersion());
			attMap.put("active", plugin.isActive());
			attMap.put("menuid", plugin.getMenuId());
			attMap.put("authurl", plugin.getAuthUrl());
			if(plugin.getImageClass()!=null && plugin.getImageClass().trim().length()>0){
				attMap.put("image", plugin.getImageClass());
			}
			attMap.put("type", "plugin");
			
			map.put("attributes", attMap);
			
			resultList.add(map);
		}
		
		
		return resultList;
	
	}
	
	@Transactional
	public List<Map<String,Object>> getPluginComboData()throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		List<Plugin> plugins = this.findAllBySortCreateDate(Direction.DESC);
		for(Plugin  plugin : plugins){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", plugin.getId());
			map.put("iconCls", "icon-none");
			map.put("text", plugin.getName());
			map.put("checked", false);
			map.put("state", "open");
			map.put("hash", "#module="+plugin.getJsModuleName()+"|{}");
			
			Map<String,Object> attMap=new HashMap<String,Object>();
			attMap.put("hash", "#module="+plugin.getJsModuleName()+"|{}");
			attMap.put("version", plugin.getVersion());
			attMap.put("active", plugin.isActive());
			attMap.put("menuid", plugin.getMenuId());
			attMap.put("authurl", plugin.getAuthUrl());
			if(plugin.getImageClass()!=null && plugin.getImageClass().trim().length()>0){
				attMap.put("image", plugin.getImageClass());
			}
			attMap.put("type", "plugin");
			
			map.put("attributes", attMap);
			
			resultList.add(map);
		}
		
		
		return resultList;
	
	}
	
}
