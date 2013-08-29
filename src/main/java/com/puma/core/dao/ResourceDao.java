package com.puma.core.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.puma.core.base.BaseDao;
import com.puma.core.domain.Resource;

public interface ResourceDao extends BaseDao<Resource, String> {
	
	/**
	 * 根据资源id的数组删除资源
	 */
	@Modifying
	@Query("delete from Resource where id in ?1")
	public void deleteByIdIn(Collection<String> ids);
}
