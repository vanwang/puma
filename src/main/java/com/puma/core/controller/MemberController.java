package com.puma.core.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.puma.common.BusinessHandler;
import com.puma.common.LogLevelEnum;
import com.puma.common.LogTypeEnum;
import com.puma.common.ResponseMessage;
import com.puma.core.annotation.LogIt;
import com.puma.core.base.BaseController;
import com.puma.core.domain.Member;
import com.puma.core.domain.Preference;
import com.puma.core.domain.Role;
import com.puma.core.security.SecurityUtils;
import com.puma.core.security.captcha.CaptchaUtil;
import com.puma.core.service.MemberService;
import com.puma.core.service.RoleService;
import com.puma.exception.BusinessException;
import com.puma.exception.DuplicatedAccountException;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RoleService roleService;
	
	@LogIt(message="获取用户列表数据选项",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="memberCache") 
	@RequestMapping("/membertable.json")
	public @ResponseBody Map<String, Object> memberTableJson(@RequestParam(value="order",required=false)String order
													,@RequestParam(value="page",required=true)Integer page
													,@RequestParam(value="rows",required=true)Integer rows
													,@RequestParam(value="sort",required=false)String sort){
		Map<String,Object> map=new HashMap<String,Object>();
		page -= 1;
		String rNum = memberService.countAll();
		map.put("total", rNum);
		
		Sort s = null;
		PageRequest pageable = null;
		if(sort == null){
			s = new Sort(Direction.DESC, "createDate");
		}else if(sort != null){
			s = new Sort(order.equalsIgnoreCase("asc")?Direction.ASC:Direction.DESC, sort);
		}
		pageable=new PageRequest(page, rows, s);
		map.put("rows", memberService.findAllByPage(pageable));
			
		return map;
	}
	
	@LogIt(message="获取用户偏好选项",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.RETRIEVE)
	@Cacheable(cacheName="themeCache") 
	@RequestMapping("/memberpreference.json")
	public @ResponseBody ResponseMessage memberPreference(){
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
					Preference p = memberService.getMemberPreference();
					return p;
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="改变皮肤风格",logLevel=LogLevelEnum.INFO,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName="themeCache",removeAll=true)
	@RequestMapping("/changetheme.do")
	public @ResponseBody ResponseMessage changeTheme(@RequestParam(value="theme",required=true)final String theme) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Member member = SecurityUtils.getAuthedMember();
				if(null == member){
					throw BusinessException.USER_NOT_LOGIN;
				}else{
					return memberService.changeTheme(theme);
				}
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="新增用户",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.CREATE)
	@TriggersRemove(cacheName="memberCache",removeAll=true)
	@RequestMapping("createnew.do")
	public @ResponseBody ResponseMessage save(final Member member, @RequestParam(value="repassword",required=true)final String repassword,
			@RequestParam(value="roleIds[]",required=false)final String[] ids) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				if(!repassword.equalsIgnoreCase(member.getPassword())){
					throw BusinessException.PASSWORD_NOT_MATCH;
				}
				String id = new Member().getId();
				member.setId(id);
				ShaPasswordEncoder sha = new ShaPasswordEncoder();  
				String passSHA = sha.encodePassword(member.getPassword(), id);
				member.setPassword(passSHA);
				
				if(!member.isSys()){
					if(null != ids){
						List<String> idList = new ArrayList<String>();
						Collections.addAll(idList, ids);
						List<Role> roleList = roleService.findByIdIn(idList);
						
						Set<Role> roleSet = new HashSet<Role>();
						Role[] a = new Role[roleList.size()];
						
						Collections.addAll(roleSet, roleList.toArray(a));
						
						member.setRoles(roleSet);
					}else{
						member.setRoles(null);
					}
				}
				member.setPreference(new Preference());
				
				memberService.save(member);
				
				return "success";
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="更新用户信息",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName={"memberCache","roleCache"},removeAll=true)
	@RequestMapping("update.do")
	public @ResponseBody ResponseMessage update(final Member member,@RequestParam(value = "roleIds[]",required=false) final String[] ids) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Member m = memberService.findById(member.getId());
				m.setName(member.getName());
				m.setEmail(member.getEmail());
				m.setActive(member.isActive());
				m.setSys(member.isSys());
				m.setModifyDate(new Date());
				
				if(!member.isSys()){
					if(null != ids){
						List<String> idList = new ArrayList<String>();
						Collections.addAll(idList, ids);
						List<Role> roleList = roleService.findByIdIn(idList);
						
						Set<Role> roleSet = new HashSet<Role>();
						Role[] a = new Role[roleList.size()];
						
						Collections.addAll(roleSet, roleList.toArray(a));
						
						m.setRoles(roleSet);
					}else{
						m.setRoles(null);
					}
				}
				
				memberService.save(m);
				
				//update stored authorities
				/*SecurityContext   ctx   =   SecurityContextHolder.getContext();              
		        Authentication   auth   =   ctx.getAuthentication();
		        
		        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		        
		        List<Resource> resources = new ArrayList<Resource>();
		        
		        Set<Role> roles = m.getRoles();
		        for(Role role:roles){
		        	Set<Resource> res = role.getResources();
		        	resources.addAll(res);
		        }
		        
		        for(Resource resource : resources){  
		        	GrantedAuthority ga = new GrantedAuthorityImpl(resource.getAuthority());  
		        	grantedAuthorities.add(ga);      
		        }  
		        m.setAuthorities(grantedAuthorities);
		        
		        UsernamePasswordAuthenticationToken newauth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),grantedAuthorities);
		        newauth.setAuthenticated(auth.isAuthenticated());
		        newauth.setDetails(auth.getDetails());
		        ctx.setAuthentication(newauth);*/
				
				return member.getId();
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="删除用户信息",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.DELETE)
	@TriggersRemove(cacheName="memberCache",removeAll=true)
	@RequestMapping("/batchdel.do")
	public @ResponseBody ResponseMessage batchDelete(@RequestParam(value = "ids[]") final String[] ids) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				List<String> idList = new ArrayList<String>();
				Collections.addAll(idList, ids);
				memberService.batchRemove(idList);
				return "success";
			}
		}, logger);
		return response;
	}
	
	@LogIt(message="重设用户密码",logLevel=LogLevelEnum.WARNING,logType=LogTypeEnum.UPDATE)
	@TriggersRemove(cacheName="memberCache",removeAll=true)
	@RequestMapping("/batchresetpass.do")
	public @ResponseBody ResponseMessage batchResetPassword(@RequestParam(value = "ids[]") final String[] ids) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				memberService.batchResetPassword(ids);
				return "success";
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("register.do")  
	public @ResponseBody ResponseMessage registerMember(final HttpServletRequest req, final HttpServletResponse res,
			final String email, final String password) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				if (!req.getMethod().equals("POST")) {
					throw new AuthenticationServiceException("Register method not supported: " + req.getMethod());
				}
				if (!CaptchaUtil.validateCaptchaByRequest(req)) {
					throw new AuthenticationServiceException("captcha error");
				}
				Member m = memberService.findMemberByEmail(email);
				if(m != null){
					throw new DuplicatedAccountException("account already exist!");
				}
				
				String activeCode = UUID.randomUUID().toString().replace("-", "");
				Member member = new Member();
				member.setEmail(email);
				member.setName(email);
				member.setActiveCode(activeCode);
				
				ShaPasswordEncoder sha = new ShaPasswordEncoder();  
				String passSHA = sha.encodePassword(password, member.getId());
				member.setPassword(passSHA);
				
				memberService.registerMember(member, req, res);
				
				return  member.getId();
			}
		}, logger);
		return response;
		
	}
	/////////////////////below are old///////////////////////////////
	
	/*@RequestMapping("update.do")
	public @ResponseBody ResponseMessage update(final Member admin, @RequestParam(value="roleIds",required=false)final String[] roleIds) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Map<String, String> data = new HashMap<String, String>();
				if (admin.getRoles() == null || admin.getRoles().size() == 0) {
					data.put("status","管理角色不允许为空!");
					return data;
				}
				Member member = memberService.findById(admin.getId());
				member.setName(admin.getName());
				member.setEmail(admin.getEmail());
				member.setActive(admin.isActive());
				member.setSys(admin.isSys());
				member.setModifyDate(new Date());
				memberService.save(member);
				
				memberRoleService.deleteByMemberId(member.getId());
				if(null != roleIds){
					for(String rId:roleIds){
						MemberRole mr = new MemberRole();
						mr.setMemberId(member.getId());
						mr.setRoleId(rId);
						memberRoleService.save(mr);
					}
				}
				
				return "success";
			}
		}, logger);
		return response;
	}*/
	
	/*@RequestMapping("advsearch.do")
	public @ResponseBody DataTableVO advSearch(final Member member
													,@RequestParam(value="sEcho",required=true)Integer sEcho
													,@RequestParam(value="iDisplayLength",required=true)Integer iDisplayLength
													,@RequestParam(value="iDisplayStart",required=true)Integer iDisplayStart) {
				int page = iDisplayStart/iDisplayLength;
				
				DataTableVO dtVO = new DataTableVO();
				dtVO.setsEcho(sEcho);
				try {
					List<Member> mList = memberService.advSearch(member, page, iDisplayLength);
					dtVO.setiTotalRecords(mList.size());
					dtVO.setiTotalDisplayRecords(memberService.advSearchCount(member));
					dtVO.setAaData(mList);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dtVO;
	}*/
	
}
