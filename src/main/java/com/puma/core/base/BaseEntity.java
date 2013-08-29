package com.puma.core.base;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

import com.puma.common.JsonDateSerializer;

/**
 * 实体类 - 基类
 */

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -6718838800112233445L;

	@SearchableId
	@Id
	@Column(length = 32, nullable = true)
/*	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")*/
	private String id;// ID
	
	@SearchableProperty(store = Store.YES)
	@Column(name = "create_date", updatable = false)
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date createDate;// 创建日期
	
	@SearchableProperty(store = Store.YES)
	@Column(name = "modify_date")
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date modifyDate;// 修改日期

	public BaseEntity() {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.createDate = new Date();
		this.modifyDate = new Date();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass().getPackage() != obj.getClass().getPackage()) {
			return false;
		}
		final BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!id.equals(other.getId())) {
			return false;
		}
		return true;
	}

}