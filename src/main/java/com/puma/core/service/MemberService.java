package com.puma.core.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.common.Email;
import com.puma.core.base.BaseService;
import com.puma.core.dao.MemberDao;
import com.puma.core.dao.MemberSpecifications;
import com.puma.core.dao.PreferenceDao;
import com.puma.core.dao.RoleDao;
import com.puma.core.domain.Member;
import com.puma.core.domain.Preference;
import com.puma.core.domain.Role;
import com.puma.core.security.SecurityUtils;

@Service("member.memberservice")
@Singleton
public class MemberService extends BaseService<Member, String>{
	
	private static String NORMAL_USER_ROLE_ID = "00000000000000000000000000000000";
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private PreferenceDao preferenceDao;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private HtmlService htmlService;
	
	@Autowired
	public void setBaseDao(MemberDao memberDao) {
		super.setBaseDao(memberDao);
	}
	
	@Transactional
	public Member findMemberByName(String username) {
		return memberDao.findMemberByName(username);
	}
	
	@Transactional
	public Member findMemberByEmail(String email) {
		return memberDao.findMemberByEmail(email);
	}
	
	@Transactional
	public boolean regVerify(String activeCode) {
		boolean isVerified = false;
		Member member = memberDao.findMemberByActiveCode(activeCode);
		if(member != null){
			member.setActive(true);
			member.setActiveCode(null);
			memberDao.save(member);
			isVerified = true;
		}
		
		return isVerified;
	}
	
	
	@Transactional
	public void batchRemove(Collection<String> ids)throws Exception {
		this.memberDao.deleteByIdIn(ids);
	}
	
	@Transactional
	public Preference getMemberPreference(){
		Preference preference = null;
		Member member = SecurityUtils.getAuthedMember();
		if(null == member){
			return new Preference();
		}
		Member m = findById(member.getId());
		if(null != member){
			try {
				preference = m.getPreference();
				preference.getId();
			} catch ( javax.persistence.EntityNotFoundException e) {
				preference = new Preference();
				m.setPreference(preference);
				save(m);
			}
		}
		return preference;
	}
	
	@Transactional
	public String changeTheme(String theme) {
		Member member = SecurityUtils.getAuthedMember();
		Member m = memberDao.findById(member.getId());
			Preference p = m.getPreference();
			p.setTheme(theme);
			preferenceDao.save(p);
			
			return p.getId();
	}
	
	@Transactional
	public void batchResetPassword(String[] ids)throws Exception {
		for(String id:ids){
			ShaPasswordEncoder sha = new ShaPasswordEncoder();  
			String passSHA = sha.encodePassword("123456", id);
			Member member = memberDao.findById(id);
			if(null != member){
				member.setPassword(passSHA);
				save(member);
			}
		}
	}
	
	@Transactional
	public Member registerMember(Member member,HttpServletRequest req, HttpServletResponse res){
		
		member.setPreference(new Preference());
		
		//普通用户ID，系统默认配置为32位0
		Role role = roleDao.findById(NORMAL_USER_ROLE_ID);
		Set<Role> roleSet = new HashSet<Role>();
		roleSet.add(role);
		member.setRoles(roleSet);
		
		memberDao.save(member);
		
		Email emailObj = new Email();
		emailObj.setSubject("PUMA账号，账号激活！");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("todayDate", dateFormat.format(member.getCreateDate()));
		data.put("regDatetime", datetimeFormat.format(member.getCreateDate()));
		data.put("expireDattimee", datetimeFormat.format(member.getCreateDate()));
		data.put("email", member.getEmail());
		data.put("activeCode", member.getActiveCode());
		
		String content = htmlService.templateToString(data);
		
		emailObj.setContent(content);
		emailObj.setAddressee(member.getEmail());
		
		try {
			mailService.sendMail(emailObj);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return member;
	}
	
	@Transactional
	public List<Member> advSearch(Member member, int page, int iDisplayLength)throws Exception {
		Sort s = new Sort(Direction.DESC, "createDate");
		PageRequest pageable =  new PageRequest(page, iDisplayLength, s);
		List<Member> mList = this.memberDao.findAll(MemberSpecifications.byMemberExample(member), pageable).getContent();
		
		return mList;
	}
	
	@Transactional
	public int advSearchCount(Member member)throws Exception {
		return (int)(this.memberDao.count(MemberSpecifications.byMemberExample(member)));
	}
	
	@Transactional
	public Set<Member> findFriends(Member member) {
		 Member m = this.memberDao.findById(member.getId());
		 Set<Member> mSet = m.getFriends();
		 return mSet;
	}
	
	@Transactional
	public Set<Member> addFriends(Member member, Set<Member> friends) {
		 Member m = this.memberDao.findById(member.getId());
		 Set<Member> mSet = m.getFriends();
		 return mSet;
	}
	
}
