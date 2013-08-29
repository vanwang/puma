package com.puma.core.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Dao接口 - Dao基类接口
 * @author Administrator
 *
 * @param <T>
 * @param <PK>
 */
public interface BaseDao<T, PK extends Serializable> extends JpaRepository<T, PK>, JpaSpecificationExecutor<T>{

	public T findById(String id);
	
	public List<T> findByIdIn(Collection<String> ids);

}
