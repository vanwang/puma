package com.puma.core.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 资源表分组（resourcegroups）
 * 表中描述的是系统需要保护的资源的分组，便于资源的管理。
 */

@Entity
@Table(name = "resourcegroups")
@JsonIgnoreProperties({"resources"})
public class Resourcegroup extends BaseEntity {

	private static final long serialVersionUID = -6614052029623997372L;
	
	@Column(name = "name")
	private String name;//资源名称
	
	
	@Column(name = "description")
	private String description;//资源描述
	
	@Column(name = "listorder")
	private int listOrder;//排序
	
	@OneToMany(mappedBy = "resourcegroup", cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private Set<Resource> resources;
	

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

	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

}