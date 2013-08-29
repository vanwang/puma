package com.puma.core.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.puma.core.base.BaseDao;
import com.puma.core.domain.Member;
import com.puma.core.domain.MemberFriend;
import com.puma.core.domain.Role;

public interface MemberFriendDao extends BaseDao<MemberFriend, String> {

	@Query("select count(*) from Member where name = :name")
	public long isExistByName(@Param("name")String username);
	
	@Query("select mf from MemberFriend mf where mf.friend.id = ?1")
	public List<MemberFriend> findByFriendId(String friendId, Pageable pageable);
}
