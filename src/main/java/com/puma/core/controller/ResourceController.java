package com.puma.core.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.puma.common.BusinessHandler;
import com.puma.common.LogLevelEnum;
import com.puma.common.LogTypeEnum;
import com.puma.common.ResponseMessage;
import com.puma.core.annotation.LogIt;
import com.puma.core.base.BaseController;
import com.puma.core.domain.Resource;
import com.puma.core.domain.Resourcegroup;
import com.puma.core.security.PumaInvocationSecurityMetadataSourceService;
import com.puma.core.service.ResourceService;
import com.puma.core.service.ResourcegroupService;

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController{
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ResourcegroupService resourcegroupService;
	
	@LogIt(message="获取资源列表",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="resourceCache") 
	@RequestMapping("/resourcetreetable.json")
	public @ResponseBody List<Map<String,Object>> getResourceTreeGridData(){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = resourcegroupService.getResourceTreeGridData();
		} catch (Exception e) {
		}
		return resultList;
	}
	
	@LogIt(message="获取资源树列表",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="resourceCache")
	@RequestMapping("/resourcetree.json")
	public @ResponseBody List<Map<String,Object>> getResourceTreeData(){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = resourcegroupService.getResourceTreeData();
		} catch (Exception e) {
		}
		return resultList;
	}
	
	@LogIt(message="新建资源",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.CREATE)
	@TriggersRemove(cacheName="resourceCache",removeAll=true)
	@RequestMapping("/create.do")
	public @ResponseBody ResponseMessage save(final Resource res,@RequestParam(value="groupid",required=true)final String groupId) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				String id = new Resource().getId();
				res.setId(id);
				
				Resourcegroup resourcegroup = resourcegroupService.findById(groupId);
				res.setResourcegroup(resourcegroup);
				
				resourceService.save(res);
				
				//刷新系统启动时候加载的资源
				List<Resource> query = resourceService.findAll();
				PumaInvocationSecurityMetadataSourceService.loadResourceDefindMethod(query);
				
				return id;
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="更新资源",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName="resourceCache",removeAll=true)
	@RequestMapping("/update.do")
	public @ResponseBody ResponseMessage update(final Resource res,@RequestParam(value="groupid",required=true)final String groupId) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Resource resource = resourceService.findById(res.getId());
				resource.setName(res.getName());
				resource.setDescription(res.getDescription());
				resource.setResourceString(res.getResourceString());
				resource.setModifyDate(new Date());
				//resource.setActive(res.isActive());
				//resource.setSys(res.isSys());
				
				Resourcegroup resourcegroup = resourcegroupService.findById(groupId);
				resource.setResourcegroup(resourcegroup);
				
				resourceService.save(resource);
				
				//刷新系统启动时候加载的资源
				List<Resource> query = resourceService.findAll();
				PumaInvocationSecurityMetadataSourceService.loadResourceDefindMethod(query);
				
				return res.getId();
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="删除资源",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.DELETE)
	@TriggersRemove(cacheName="resourceCache",removeAll=true)
	@RequestMapping("/deleteresource.do")
	public @ResponseBody ResponseMessage deleteResource(@RequestParam(value = "id") final String id) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				resourceService.remove(id);
				
				//刷新系统启动时候加载的资源
				List<Resource> query = resourceService.findAll();
				PumaInvocationSecurityMetadataSourceService.loadResourceDefindMethod(query);
				return id;
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="更新资源组",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.DELETE)
	@TriggersRemove(cacheName="resourceCache",removeAll=true)
	@RequestMapping("/deleteresourceingroup.do")
	public @ResponseBody ResponseMessage deleteResourceInGroup(@RequestParam(value = "id") final String groupId) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				//Resourcegroup resourcegroup = resourcegroupService.findById(groupId);
				resourcegroupService.remove(groupId);
				return groupId;
			}
		}, logger);
		return response;
	}
	////before are done
	
	/*@RequestMapping("/advsearch.do")
	public @ResponseBody DataTableVO advSearch(final Resource resource
													,@RequestParam(value="sEcho",required=true)Integer sEcho
													,@RequestParam(value="iDisplayLength",required=true)Integer iDisplayLength
													,@RequestParam(value="iDisplayStart",required=true)Integer iDisplayStart) {
				int page = iDisplayStart/iDisplayLength;
				
				DataTableVO dtVO = new DataTableVO();
				dtVO.setsEcho(sEcho);
				try {
					List<Resource> mList = resourceService.advSearch(resource, page, iDisplayLength);
					dtVO.setiTotalRecords(mList.size());
					dtVO.setiTotalDisplayRecords(resourceService.advSearchCount(resource));
					dtVO.setAaData(mList);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dtVO;
	}*/
	
}
