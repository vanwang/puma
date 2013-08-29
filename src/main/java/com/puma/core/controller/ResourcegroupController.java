package com.puma.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
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
import com.puma.core.domain.Resourcegroup;
import com.puma.core.service.ResourcegroupService;

@Controller
@RequestMapping("/resourcegroup")
public class ResourcegroupController extends BaseController{
	
	@Autowired
	private ResourcegroupService resourcegroupService;
	
	@Cacheable(cacheName="resourceGroupCache")
	@RequestMapping("/combodata.json")
	public @ResponseBody List<Resourcegroup> comboData(){
		List<Resourcegroup> rgs = new ArrayList<Resourcegroup>();
		Resourcegroup first = new Resourcegroup();
		first.setId("");
		first.setName("请选择分组");
		rgs.add(first);
		
		rgs.addAll(resourcegroupService.findAllBySortCreateDate(Direction.DESC));
		
		Resourcegroup last = new Resourcegroup();
		last.setId("create-group");
		last.setName("新建分组");
		rgs.add(last);
		return rgs;
	}
	
	@LogIt(message="新建资源分组",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.CREATE)
	@TriggersRemove(cacheName={"resourceGroupCache","resourceCache"},removeAll=true)
	@RequestMapping("/createnew.do")
	public @ResponseBody ResponseMessage createNewGroup(@RequestParam(value = "name") final String name){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Resourcegroup rp = new Resourcegroup();
				rp.setName(name);
				return resourcegroupService.save(rp).getId();
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="更新资源组名称",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName={"resourceGroupCache","resourceCache"},removeAll=true)
	@RequestMapping("/updatename.do")
	public @ResponseBody ResponseMessage updateGroupName(@RequestParam(value = "id") final String groupId, @RequestParam(value = "name") final String name){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Resourcegroup rp = resourcegroupService.findById(groupId);
				rp.setName(name);
				return resourcegroupService.save(rp).getId();
			}
		}, logger);
		return response;
	}
	
	
}
