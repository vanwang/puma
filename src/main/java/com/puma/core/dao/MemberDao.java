package com.puma.core.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.puma.core.base.BaseDao;
import com.puma.core.domain.Member;

public interface MemberDao extends BaseDao<Member, String>{

	/**
	 * 根据用户名判断此用户是否存在（不区分大小写）
	 * 
	 */
	@Query("select count(*) from Member where name = :name")
	public long isExistByName(@Param("name")String username);
	
	/**
	 * 根据用户名获取管理员对象，若管理员不存在，则返回null（不区分大小写）
	 * 
	 */
	public Member findMemberByName(String username);
	
	/**
	 * 根据激活码获取人员对象，若人员不存在，则返回null（不区分大小写）
	 * 
	 */
	public Member findMemberByActiveCode(String activeCode);
	
	/**
	 * 根据用户邮件名称获取管理员对象，若管理员不存在，则返回null（不区分大小写）
	 * 
	 */
	public Member findMemberByEmail(String email);
	
	/**
	 * 根据用户id的数组重设用户密码
	 */
	@Modifying
	@Query("update Member m set m.password=?2 where id in ?1")
	public void updateUserPassByIdIn(Collection<String> ids, String newpass);
	
	/**
	 * 根据用户id的数组删除用户
	 */
	@Modifying
	@Query("delete from Member where id in ?1")
	public void deleteByIdIn(Collection<String> ids);
	
	//@Query("select a from Authority a where a.id in (select ra.authorityId from RoleAuthority ra where ra.roleId in (select r.id from MemberRole mr, Member m, Role r where mr.memberId = m.id and mr.roleId = r.id and m.id = ?1))")
	//public List<Authority> findMemberRoles(String memberId);
	
	//@Query("select r.id from Member m , MemberRole mr , Role r where m.id = mr.memberId and r.id = mr.roleId and m.id = ?1")
	//public List<String> getRoleIds(String memberId);
}
