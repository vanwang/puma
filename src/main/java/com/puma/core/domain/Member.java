package com.puma.core.domain;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.puma.common.JsonDateSerializer;
import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 用户
 */

@Entity
@Table(name = "members")
@JsonIgnoreProperties({"roles","password","authorities","preference","logs","friends","hibernateLazyInitializer","handler"})
public class Member extends BaseEntity implements UserDetails{

	private static final long serialVersionUID = -7519486823153844426L;
	
	@Column(name = "name", nullable = false)
	private String name;// 用户名
	
	@Column(name = "password", nullable = false)
	private String password;// 密码
	
	@Column(name = "email", length = 255, nullable = false, unique = true)
	private String email;// E-mail
	
	@Column(name = "active", nullable = false)
	private boolean active; //锁定状态
	
	@Column(name = "active_code", length = 32)
	private String activeCode;// E-mail
	
	@Column(name = "sys", nullable = false)
	private boolean sys; //是否是超级权限
	
	@Column(name = "last_connect", nullable = false)
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date lastConnect;// 上次登录日期
	
	@ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)   
	@JoinTable(name = "members_roles",
			joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles;
	
	@ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)   
	@JoinTable(name = "members_friends",
			joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
	private Set<Member> friends;
	
	@OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private Set<Log> logs;
	
	/*@OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private Set<DaylogCalendar> daylogCalendars;*/
	
	/*@OneToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)   
	@JoinTable(name="members_preferences",
			joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "preference_id", referencedColumnName = "id"))*/
	/*@OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)*/
	/*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name="preferences",
			inverseJoinColumns = @JoinColumn(name = "id"),
			joinColumns = @JoinColumn(name = "pid"))*/
	
	@OneToOne(cascade={CascadeType.ALL}, fetch = FetchType.LAZY)  
    @JoinColumn(name="pid") 
    //@NotFound(action=NotFoundAction.IGNORE)
	private Preference preference;
	
	@Transient
	private Collection<GrantedAuthority> authorities;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public boolean isSys() {
		return sys;
	}

	public void setSys(boolean sys) {
		this.sys = sys;
	}

	public Date getLastConnect() {
		return lastConnect;
	}

	public void setLastConnect(Date lastConnect) {
		this.lastConnect = lastConnect;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Preference getPreference() {
		return preference;
	}

	public void setPreference(Preference preference) {
		this.preference = preference;
	}

	public Set<Log> getLogs() {
		return logs;
	}

	public void setLogs(Set<Log> logs) {
		this.logs = logs;
	}

	/*public Set<DaylogCalendar> getDaylogCalendars() {
		return daylogCalendars;
	}

	public void setDaylogCalendars(Set<DaylogCalendar> daylogCalendars) {
		this.daylogCalendars = daylogCalendars;
	}*/

	public Set<Member> getFriends() {
		return friends;
	}

	public void setFriends(Set<Member> friends) {
		this.friends = friends;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}