package com.puma.core.service;

import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.ResourceDao;
import com.puma.core.dao.ResourceSpecifications;
import com.puma.core.domain.Resource;

@Service("resource.resourceservice")
@Singleton
public class ResourceService extends BaseService<Resource, String>{

	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	public void setBaseDao(ResourceDao resourceDao) {
		super.setBaseDao(resourceDao);
	}
	
	@Transactional
	public void batchRemove(Collection<String> ids)throws Exception {
		this.resourceDao.deleteByIdIn(ids);
	}
	
	@Transactional
	public List<Resource> advSearch(Resource resource, int page, int iDisplayLength)throws Exception {
		Sort s = new Sort(Direction.ASC, "resourceString");
		PageRequest pageable =  new PageRequest(page, iDisplayLength, s);
		List<Resource> rList = this.resourceDao.findAll(ResourceSpecifications.byResourceExample(resource), pageable).getContent();
		
		return rList;
	}
	
	@Transactional
	public int advSearchCount(Resource resource)throws Exception {
		return (int)(this.resourceDao.count(ResourceSpecifications.byResourceExample(resource)));
	}
}
