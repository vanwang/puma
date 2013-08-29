package com.puma.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.ResourcegroupDao;
import com.puma.core.domain.Resource;
import com.puma.core.domain.Resourcegroup;

@Service("resource.resourcegroupservice")
@Singleton
public class ResourcegroupService extends BaseService<Resourcegroup, String>{

	@Autowired
	private ResourceService resourceService;
	
	@SuppressWarnings("unused")
	@Autowired
	private ResourcegroupDao resourcegroupDao;
	
	@Autowired
	public void setBaseDao(ResourcegroupDao resourcegroupDao) {
		super.setBaseDao(resourcegroupDao);
	}
	
	@Transactional
	public List<Map<String,Object>> getResourceTreeData()throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		List<Resource> allResources = resourceService.findAllBySortCreateDate(Direction.DESC);
		List<Resourcegroup> resourcegroupList = findAllBySortCreateDate(Direction.DESC);
		for(Resourcegroup  resourcegroup : resourcegroupList){
			List<Map<String,Object>> resourceList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", resourcegroup.getId());
			map.put("iconCls", "icon-none");
			
			Map<String,Object> attMap=new HashMap<String,Object>();
			attMap.put("type", "group");
			
			map.put("attributes", attMap);
			
			Set<Resource> resources = resourcegroup.getResources();
			map.put("text", resourcegroup.getName() + " (<span style='color:red;font-weight:bold;'>"+resources.size()+"</span>)");
			
			allResources.removeAll(resources);
			for(Resource resource : resources){
				Map<String,Object> rmap=new HashMap<String,Object>();
				rmap.put("id", resource.getId());
				rmap.put("text", resource.getName());
				rmap.put("iconCls", "icon-none");
				
				Map<String,Object> attRmap=new HashMap<String,Object>();
				attRmap.put("url", resource.getResourceString());
				//attRmap.put("sys", resource.isSys());
				//attRmap.put("active", resource.isActive());
				attRmap.put("description", resource.getDescription());
				attRmap.put("groupid", resource.getResourcegroup().getId());
				attRmap.put("type", "resource");
				
				rmap.put("attributes", attRmap);
				
				resourceList.add(rmap);
			}
			map.put("children", resourceList);
			resultList.add(map);
		}
		if(allResources.size() > 0){
			List<Map<String,Object>> resourceList = new ArrayList<Map<String,Object>>();
			Map<String,Object> ungroupMap=new HashMap<String,Object>();
			ungroupMap.put("text", "未分组  (<span style='color:red;font-weight:bold;'>"+allResources.size()+"</span>)");
			ungroupMap.put("iconCls", "icon-none");
			
			Map<String,Object> attMap=new HashMap<String,Object>();
			attMap.put("type", "group");
			
			ungroupMap.put("attributes", attMap);
			for(Resource resource : allResources){
				Map<String,Object> rmap=new HashMap<String,Object>();
				rmap.put("id", resource.getId());
				rmap.put("text", resource.getName());
				rmap.put("iconCls", "icon-none");
				
				Map<String,Object> attRmap=new HashMap<String,Object>();
				attRmap.put("url", resource.getResourceString());
				//attRmap.put("sys", resource.isSys());
				//attRmap.put("active", resource.isActive());
				attRmap.put("description", resource.getDescription());
				//attRmap.put("groupid", resource.getResourcegroup().getId());
				attRmap.put("type", "resource");
				
				rmap.put("attributes", attRmap);
				
				resourceList.add(rmap);
			}
			ungroupMap.put("children", resourceList);
			resultList.add(ungroupMap);
		}
		
		
		return resultList;
	
	}
	
	@Transactional
	public List<Map<String,Object>> getResourceTreeGridData()throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		List<Resource> allResources = resourceService.findAllBySortCreateDate(Direction.DESC);
		List<Resourcegroup> resourcegroupList = findAllBySortCreateDate(Direction.DESC);
		for(Resourcegroup  resourcegroup : resourcegroupList){
			List<Map<String,Object>> resourceList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", resourcegroup.getId());
			map.put("type", "group");
			map.put("iconCls", "icon-none");
			//map.put("state", "closed");
			
			Set<Resource> resources = resourcegroup.getResources();
			map.put("name", resourcegroup.getName() + " (<span style='color:red;font-weight:bold;'>"+resources.size()+"</span>)");
			
			allResources.removeAll(resources);
			for(Resource resource : resources){
				Map<String,Object> rmap=new HashMap<String,Object>();
				rmap.put("id", resource.getId());
				rmap.put("name", resource.getName());
				rmap.put("url", resource.getResourceString());
				//rmap.put("sys", resource.isSys());
				//rmap.put("active", resource.isActive());
				rmap.put("description", resource.getDescription());
				rmap.put("groupid", resource.getResourcegroup().getId());
				rmap.put("type", "resource");
				rmap.put("iconCls", "icon-none");
				resourceList.add(rmap);
			}
			map.put("children", resourceList);
			resultList.add(map);
		}
		if(allResources.size() > 0){
			List<Map<String,Object>> resourceList = new ArrayList<Map<String,Object>>();
			Map<String,Object> ungroupMap=new HashMap<String,Object>();
			ungroupMap.put("id", "no-group");
			ungroupMap.put("name", "未分组  (<span style='color:red;font-weight:bold;'>"+allResources.size()+"</span>)");
			ungroupMap.put("type", "group");
			//ungroupMap.put("state", "closed");
			ungroupMap.put("iconCls", "icon-none");
			
			for(Resource resource : allResources){
				Map<String,Object> rmap=new HashMap<String,Object>();
				rmap.put("id", resource.getId());
				rmap.put("name", resource.getName());
				rmap.put("iconCls", "icon-none");
				rmap.put("url", resource.getResourceString());
				//rmap.put("sys", resource.isSys());
				//rmap.put("active", resource.isActive());
				rmap.put("description", resource.getDescription());
				rmap.put("type", "resource");
				
				resourceList.add(rmap);
			}
			ungroupMap.put("children", resourceList);
			resultList.add(ungroupMap);
		}
		
		return resultList;
	
	}
}
