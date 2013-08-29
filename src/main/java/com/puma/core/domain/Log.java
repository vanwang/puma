package com.puma.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.puma.common.LogLevelEnum;
import com.puma.common.LogTypeEnum;
import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 日志表（logs）
 * 表中描述的是用户操作的记录。
 */

@Entity
@Table(name = "logs")
@JsonIgnoreProperties({"member"})
public class Log extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 795389668397883809L;

	@Column(name = "message")
	private String message;
	
	@Column(name = "m_name")
	private String mname;
	
	@Column(name = "m_email")
	private String memail;
	
	@Column(name = "type")
	private LogTypeEnum type;
	
	@Column(name = "level")
	private LogLevelEnum level;
	
	@Column(name = "status")
	private int status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mid", referencedColumnName = "id")
	private Member member;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LogTypeEnum getType() {
		return type;
	}

	public void setType(LogTypeEnum type) {
		this.type = type;
	}

	public LogLevelEnum getLevel() {
		return level;
	}

	public void setLevel(LogLevelEnum level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getMemail() {
		return memail;
	}

	public void setMemail(String memail) {
		this.memail = memail;
	}

	

}