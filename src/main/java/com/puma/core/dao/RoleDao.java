package com.puma.core.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.puma.core.base.BaseDao;
import com.puma.core.domain.Role;

public interface RoleDao extends BaseDao<Role, String> {

	/**
	 * 根据角色id的数组删除角色
	 */
	@Modifying
	@Query("delete from Role where id in ?1")
	public void deleteByIdIn(Collection<String> ids);
	
}
