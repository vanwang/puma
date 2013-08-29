package com.puma.core.service;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puma.core.base.BaseService;
import com.puma.core.dao.MenuDao;
import com.puma.core.domain.Menu;

@Service("menu.menuservice")
@Singleton
public class MenuService extends BaseService<Menu, String>{

	@SuppressWarnings("unused")
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	public void setBaseDao(MenuDao menuDao) {
		super.setBaseDao(menuDao);
	}
}
