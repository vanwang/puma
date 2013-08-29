package com.puma.core.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.puma.core.base.BaseDao;
import com.puma.core.domain.Log;

public interface LogDao extends BaseDao<Log, String> {
	
	/**
	 * 根据用户日志id的数组删除日志
	 */
	@Modifying
	@Query("delete from Log where id in ?1")
	public void deleteByIdIn(Collection<String> ids);
}
