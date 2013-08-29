package com.puma.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 插件表,前台称呼为模块（plugins）
 * 表中描述的是用户操作的记录。
 */

@Entity
@Table(name = "plugins")
public class Plugin extends BaseEntity{

	private static final long serialVersionUID = -68234150622556238L;

	@Column(name = "name")
	private String name;
	
	//the javascript module name eg. UserManagementModule
	@Column(name = "js_module_name")
	private String jsModuleName;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "menu_id")
	private String menuId;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "dependency")
	private String dependency;
	
	@Column(name = "image_class")
	private String imageClass;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "auth_url")
	private String authUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public String getJsModuleName() {
		return jsModuleName;
	}

	public void setJsModuleName(String jsModuleName) {
		this.jsModuleName = jsModuleName;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public String getImageClass() {
		return imageClass;
	}

	public void setImageClass(String imageClass) {
		this.imageClass = imageClass;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}