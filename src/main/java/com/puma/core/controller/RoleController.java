package com.puma.core.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import com.puma.core.domain.Role;
import com.puma.core.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
	
	@Autowired
	private RoleService roleService;
	
	@LogIt(message="获取所有角色树数据",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="roleCache")
	@RequestMapping("/roletree.json")
	public @ResponseBody List<Map<String,Object>> roleTreeJson(){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Sort s = new Sort(Direction.DESC, "createDate");
		List<Role> roleList = roleService.findAllBySort(s);
		
		for(Role role:roleList){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", role.getId());
			map.put("text", role.getName());
			map.put("iconCls", "icon-none");
			
			Map<String,Object> attRmap=new HashMap<String,Object>();
			//attRmap.put("sys", role.isSys());
			//attRmap.put("active", role.isActive());
			attRmap.put("description", role.getDescription());
			
			map.put("attributes", attRmap);
			
			resultList.add(map);
		}
		
		return resultList;
	}
	
	@LogIt(message="获取用户角色数据",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="roleCache")
	@RequestMapping("/memberroletree.json")
	public @ResponseBody List<Map<String,Object>> memberRoleTreeJson(@RequestParam(value = "id") final String memberId){
			return roleService.getRoleJsonWithMemberRoles(memberId);
	}
	
	@LogIt(message="关联资源到角色",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName={"roleCache"},removeAll=true)
	@RequestMapping("/combineresource.do")
	public @ResponseBody ResponseMessage combineResource(@RequestParam(value = "ids[]",required=false) final String[] ids,
														 @RequestParam(value = "roleId") final String roleId) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				return roleService.combineResource(ids, roleId);
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="获取角色的资源信息",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="roleCache")
	@RequestMapping("/selectedresource.do")
	public @ResponseBody ResponseMessage selectedResource(@RequestParam(value = "roleId") final String roleId) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				return roleService.getRoleCombinedResrouceIds(roleId);
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="创建新的角色",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.CREATE)
	@TriggersRemove(cacheName="roleCache",removeAll=true)
	@RequestMapping("/createnew.do")
	public @ResponseBody ResponseMessage createNew(@RequestParam(value="name")final String name) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Role role = new Role();
				String id = role.getId();
				role.setName(name);
				roleService.save(role);
				return id;
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="更新角色信息",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName="roleCache",removeAll=true)
	@RequestMapping("/update.do")
	public @ResponseBody ResponseMessage update(final Role r) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Role role = roleService.findById(r.getId());
				role.setName(r.getName());
				role.setDescription(r.getDescription());
				role.setModifyDate(new Date());
				//role.setActive(r.isActive());
				//role.setSys(r.isSys());
				roleService.save(role);
				return role.getId();
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="删除角色信息",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.DELETE)
	@TriggersRemove(cacheName="roleCache",removeAll=true)
	@RequestMapping("/batchdel.do")
	public @ResponseBody ResponseMessage batchDelete(@RequestParam(value = "ids[]") final String[] ids) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				List<String> idList = new ArrayList<String>();
				Collections.addAll(idList, ids);
				roleService.batchRemove(idList);
				return "success";
			}
		}, logger);
		return response;
	}
	
	/////////////////////////below are old/////////////////////////////////////
	
	/*@RequestMapping("/advsearch.do")
	public @ResponseBody DataTableVO advSearch(final Role role
													,@RequestParam(value="sEcho",required=true)Integer sEcho
													,@RequestParam(value="iDisplayLength",required=true)Integer iDisplayLength
													,@RequestParam(value="iDisplayStart",required=true)Integer iDisplayStart) {
				int page = iDisplayStart/iDisplayLength;
				
				DataTableVO dtVO = new DataTableVO();
				dtVO.setsEcho(sEcho);
				try {
					List<Role> mList = roleService.advSearch(role, page, iDisplayLength);
					dtVO.setiTotalRecords(mList.size());
					dtVO.setiTotalDisplayRecords(roleService.advSearchCount(role));
					dtVO.setAaData(mList);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dtVO;
	}*/
	
}
