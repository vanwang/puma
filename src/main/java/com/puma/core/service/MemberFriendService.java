package com.puma.core.service;

import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.LogDao;
import com.puma.core.dao.MemberFriendDao;
import com.puma.core.domain.Log;
import com.puma.core.domain.Member;
import com.puma.core.domain.MemberFriend;
import com.puma.core.domain.Preference;
import com.puma.core.security.SecurityUtils;

@Service("memberfriend.memberfriendservice")
@Singleton
public class MemberFriendService extends BaseService<MemberFriend, String>{

	@Autowired
	private MemberFriendDao memberFriendDao;
	
	@Autowired
	public void setBaseDao(MemberFriendDao memberFriendDao) {
		super.setBaseDao(memberFriendDao);
	}
	
	@Transactional
	public List<MemberFriend> findByFriendId(String friendId, Pageable pageable) {
		return memberFriendDao.findByFriendId(friendId, pageable);
	}
	
}
