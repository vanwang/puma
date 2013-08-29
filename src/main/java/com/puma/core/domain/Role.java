package com.puma.core.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 角色表（roles）
 * 表中描述的是系统按用户分类或按照功能模块分类，将系统进行整合归类管理。
 */

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

	private static final long serialVersionUID = -6614052029623997372L;
	
	@Column(name = "name")
	private String name;//角色名称
	
	@Column(name = "description")
	private String description;//角色描述
	
	/*@Column(name = "active", nullable = false)
	private boolean active; //是否被禁用
	
	@Column(name = "sys", nullable = false)
	private boolean sys; //是否是超级权限*/

	@ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)   
	@JoinTable(name = "roles_resources",
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"))
	private Set<Resource> resources;
	
	@ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)   
	@JoinTable(name = "members_roles",
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "Member_id", referencedColumnName = "id"))
	private Set<Member> members;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}
	
}