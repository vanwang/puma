package com.puma.core.service;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puma.core.base.BaseService;
import com.puma.core.dao.PreferenceDao;
import com.puma.core.domain.Preference;

@Service("preference.preferenceservice")
@Singleton
public class PreferenceService extends BaseService<Preference, String>{

	@SuppressWarnings("unused")
	@Autowired
	private PreferenceDao preferenceDao;
	
	@Autowired
	public void setBaseDao(PreferenceDao preferenceDao) {
		super.setBaseDao(preferenceDao);
	}
	
}
