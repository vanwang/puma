package com.puma.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 用户偏好表（preferences）
 * 表中描述的是用户的偏好选项。
 */

@Entity
@Table(name = "preferences")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonAutoDetect
public class Preference extends BaseEntity {

	private static final long serialVersionUID = -8217344203444539528L;
	
	@Column(name = "theme")
	private String theme;//皮肤名称
	
	public Preference(){
		super();
		this.theme = "default";
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

}