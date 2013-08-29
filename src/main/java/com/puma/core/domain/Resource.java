package com.puma.core.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 资源表（resources）
 * 表中描述的是系统需要保护的资源及（url或方法）。
 */

@Entity
@Table(name = "resources")
public class Resource extends BaseEntity implements GrantedAuthority{

	private static final long serialVersionUID = -6614052029623997372L;
	
	@Column(name = "name")
	private String name;//资源名称
	
	@Column(name = "type")
	private String type;//资源类型url、method
	
	@Column(name = "priority")
	private int priority;//资源优先权	即排序
	
	@Column(name = "resource_string", unique = true)
	private String resourceString;//资源链接
	
	@Column(name = "description")
	private String description;//资源描述
	
	/*@Column(name = "active", nullable = false)
	private boolean active; //是否被禁用
	
	@Column(name = "sys", nullable = false)
	private boolean sys; //是否是管理后台权限*/	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gid", referencedColumnName = "id")
	private Resourcegroup resourcegroup;

	@ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)   
	@JoinTable(name = "roles_resources",
			joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getResourceString() {
		return resourceString;
	}

	public void setResourceString(String resourceString) {
		this.resourceString = resourceString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSys() {
		return sys;
	}

	public void setSys(boolean sys) {
		this.sys = sys;
	}*/

	public Resourcegroup getResourcegroup() {
		return resourcegroup;
	}

	public void setResourcegroup(Resourcegroup resourcegroup) {
		this.resourcegroup = resourcegroup;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String getAuthority() {
		//return name;
		return resourceString;
	}

}