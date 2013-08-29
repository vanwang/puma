package com.puma.core.service;

import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.LogDao;
import com.puma.core.domain.Log;

@Service("log.logservice")
@Singleton
public class LogService extends BaseService<Log, String>{

	@Autowired
	private LogDao logDao;
	
	@Autowired
	public void setBaseDao(LogDao logDao) {
		super.setBaseDao(logDao);
	}
	
	@Transactional
	public void batchRemove(Collection<String> ids)throws Exception {
		this.logDao.deleteByIdIn(ids);
	}
}
