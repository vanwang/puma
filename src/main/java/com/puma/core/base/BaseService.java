package com.puma.core.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

/**
 * service抽象父类 - service基类
 * 
 * @author Administrator
 * 
 * @param <T>
 * @param <PK>
 */
public abstract class BaseService<T, PK extends Serializable> {

	private BaseDao<T, PK> baseDao;

	public BaseDao<T, PK> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao<T, PK> baseDao) {
		this.baseDao = baseDao;
	}

	@Transactional
	public T findById(String id) {
		return this.baseDao.findById(id);
	}

	@Transactional
	public List<T> findByIdIn(Collection<String> ids) {
		return this.baseDao.findByIdIn(ids);
	}

	@Transactional
	public List<T> findAllByPage(Pageable pageable) {
		return this.baseDao.findAll(pageable).getContent();
	}
	
	@Transactional
	public List<T> findAllBySortCreateDate(Direction direction) {
		Sort s = new Sort(direction, "createDate");
		return this.baseDao.findAll(s);
	}
	
	@Transactional
	public List<T> findAllBySort(Sort s) {
		return this.baseDao.findAll(s);
	}

	@Transactional
	public List<T> findAll() {
		return this.baseDao.findAll();
	}

	@Transactional
	public String countAll() {
		return Long.toString(this.baseDao.count());
	}

	@Transactional
	public String remove(String id) throws Exception {
		this.baseDao.delete(this.baseDao.findById(id));
		return id;
	}

	@Transactional
	public T save(T entity) {
		return this.baseDao.save(entity);
	}
	
	@Transactional
	public List<T> save(Iterable<T> entities) {
		return this.baseDao.save(entities);
	}
}
