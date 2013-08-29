package com.puma.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 系统菜单表（menus）
 * 用户定义菜单
 */

@Entity
@Table(name = "menus")
public class Menu extends BaseEntity{

	private static final long serialVersionUID = 1971797097984278097L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "is_default")
	private boolean isDefault;
	
	@Column(name = "menu_json")
	private String menuJson;
	
	@Column(name = "m_order")
	private int menuOrder;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getMenuJson() {
		return menuJson;
	}

	public void setMenuJson(String menuJson) {
		this.menuJson = menuJson;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}
	
}