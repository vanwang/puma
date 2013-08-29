package com.puma.core.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.puma.core.base.BaseEntity;

/**
 * 实体类 - 用户好友关联表（members_friends）
 */

@Entity
@Table(name = "members_friends")
public class MemberFriend extends BaseEntity {

	private static final long serialVersionUID = -6614052029623997372L;
	
	/*@Column(name = "member_id")
	private String memberId;//角色名称*/
	@OneToOne(cascade={CascadeType.ALL}, fetch = FetchType.LAZY)  
    @JoinColumn(name="member_id",referencedColumnName = "id") 
	private Member member;
	
	
	/*@Column(name = "friend_id")
	private String friendId;//角色描述*/
	@OneToOne(cascade={CascadeType.ALL}, fetch = FetchType.LAZY)  
    @JoinColumn(name="friend_id",referencedColumnName = "id") 
	private Member friend;


	public Member getMember() {
		return member;
	}


	public void setMember(Member member) {
		this.member = member;
	}


	public Member getFriend() {
		return friend;
	}


	public void setFriend(Member friend) {
		this.friend = friend;
	}
	
	
	
}