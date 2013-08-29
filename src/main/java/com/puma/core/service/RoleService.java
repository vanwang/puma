package com.puma.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.MemberDao;
import com.puma.core.dao.ResourceDao;
import com.puma.core.dao.RoleDao;
import com.puma.core.dao.RoleSpecifications;
import com.puma.core.domain.Member;
import com.puma.core.domain.Resource;
import com.puma.core.domain.Role;

@Service("role.roleservice")
@Singleton
public class RoleService extends BaseService<Role, String>{

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	public void setBaseDao(RoleDao roleDao) {
		super.setBaseDao(roleDao);
	}
	
	@Transactional
	public void batchRemove(Collection<String> ids)throws Exception {
		this.roleDao.deleteByIdIn(ids);
	}

	@Transactional
	public List<Role> advSearch(Role role, int page, int iDisplayLength)throws Exception {
		Sort s = new Sort(Direction.DESC, "createDate");
		PageRequest pageable =  new PageRequest(page, iDisplayLength, s);
		List<Role> rList = this.roleDao.findAll(RoleSpecifications.byRoleExample(role), pageable).getContent();
		
		return rList;
	}
	
	@Transactional
	public int advSearchCount(Role role)throws Exception {
		return (int)(this.roleDao.count(RoleSpecifications.byRoleExample(role)));
	}
	
	@Transactional
	public List<Map<String,Object>> getRoleJsonWithMemberRoles(String memberId){
		Member member = memberDao.findById(memberId);
		Set<Role> roles = null;
		if(member != null){
			roles = member.getRoles();
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Sort s = new Sort(Direction.DESC, "createDate");
		List<Role> roleList = findAllBySort(s);
		
		for(Role role:roleList){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", role.getId());
			map.put("text", role.getName());
			map.put("iconCls", "icon-none");
			if(roles != null){
				for(Role r:roles){
					if(r.getId().equals(role.getId())){
						map.put("checked", true);
					}
				}
			}
			
			Map<String,Object> attRmap=new HashMap<String,Object>();
			//attRmap.put("sys", role.isSys());
			//attRmap.put("active", role.isActive());
			attRmap.put("description", role.getDescription());
			
			map.put("attributes", attRmap);
			
			resultList.add(map);
		}
		
		return resultList;
	}
	
	@Transactional
	public String combineResource(String[] ids, String roleId)throws Exception {

		Role role = roleDao.findById(roleId);
		
		if(null == ids){
			role.setResources(new HashSet<Resource>());
		}else{
			List<String> idList = new ArrayList<String>();
			Collections.addAll(idList, ids);
			
			List<Resource> resources = resourceDao.findByIdIn(idList);
			
			Set<Resource> resourceSet = new HashSet<Resource>();
			Resource[] a = new Resource[resourceSet.size()];
			
			Collections.addAll(resourceSet, resources.toArray(a));
			
			role.setResources(resourceSet);
		}
		
		save(role);
		
		return role.getId();
	}
	
	@Transactional
	public List<String> getRoleCombinedResrouceIds(String roleId)throws Exception {

		Role role = roleDao.findById(roleId);
		Set<Resource> resources = role.getResources();
		List<String> re = new ArrayList<String>();
		for(Resource resource:resources){
			re.add(resource.getId());
		}
		
		return re;
	}
}
