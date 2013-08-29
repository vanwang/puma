package com.puma.plugin.daylog.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puma.core.base.BaseService;
import com.puma.core.dao.MemberDao;
import com.puma.core.domain.Member;
import com.puma.plugin.daylog.dao.DaylogCalendarDao;
import com.puma.plugin.daylog.domain.DaylogCalendar;

@Service("dl.calendarservice")
@Singleton
public class DaylogCalendarService extends BaseService<DaylogCalendar, String>{
	
	@Autowired
	private DaylogCalendarDao dlCalendarDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	public void setBaseDao(DaylogCalendarDao dlCalendarDao) {
		super.setBaseDao(dlCalendarDao);
	}
	
	/*@Transactional
	public String save(DaylogCalendar daylogCalendar, UserAlarm userAlarm, boolean isQuickAdd) {
		DaylogCalendar dc = dlCalendarDao.findById(daylogCalendar.getId());
		if(null != dc){
			daylogCalendar.setCreateDate(dc.getCreateDate());
		}else{
			daylogCalendar.setCreateDate(new Date());
		}
		
		
		if(!isQuickAdd && !daylogCalendar.getIsDraft()){
			userAlarm.setBlogId(daylogCalendar.getId());
			userAlarm.setUserId(userId);
			UserAlarm ua = userAlarmDAO.findByUserIdAndBlogId(userId, daylogCalendar.getId());
			if(null == ua){
				if(!userAlarm.getAlarmWhen().equalsIgnoreCase("00")){
					userAlarmDAO.save(userAlarm);
				}
			}else{
				userAlarm.setId(ua.getId());
				if(!userAlarm.getAlarmWhen().equalsIgnoreCase("00")){
					userAlarmDAO.save(userAlarm);
				}else{
					userAlarmDAO.delete(ua.getId());
				}
			}
			
			
			if(!daylogCalendar.getIsDraft())
			{
				try {
					if(userAlarm.getAlarmWhen().substring(0, 1).equalsIgnoreCase("1")){
						schedulerRemoteService.addTrigger(true, daylogCalendar.getUserId(), daylogCalendar.getId(), daylogCalendar.getStartTime(), daylogCalendar.getSubject(), daylogCalendar.getBlog());
					}
					if(userAlarm.getAlarmWhen().substring(1, 2).equalsIgnoreCase("1")){
						schedulerRemoteService.addTrigger(false, daylogCalendar.getUserId(), daylogCalendar.getId(), daylogCalendar.getEndTime(), daylogCalendar.getSubject(), daylogCalendar.getBlog());
					}
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
		}
		
		return daylogCalendar.getId();
	}*/
	
	/*public String quickAddTrigger(boolean isstart, String blogId, String use, boolean value){
		DaylogUser me = Security.getAuthedDaylogUser();
		String myId = me.getId();
		
		DaylogCalendar dc = dlCalendarDAO.findById(blogId);
		
		UserAlarm ua = userAlarmDAO.findByUserIdAndBlogId(myId, blogId);
		if(null == ua){
			String startAlarmType = "000";
			String endAlarmType = "000";
			ua = new UserAlarm();
			ua.setUserId(myId);
			ua.setBlogId(blogId);
			
			String tmp = "000";
			if(use.equalsIgnoreCase("email")){
				tmp = (value?"100":"000");
			}else if(use.equalsIgnoreCase("sms")){
				tmp = (value?"010":"000");
			}else if(use.equalsIgnoreCase("call")){
				tmp = (value?"001":"000");
			}
			if(isstart){
				ua.setAlarmWhen("10");
				startAlarmType = tmp;
			}else{
				ua.setAlarmWhen("01");
				endAlarmType = tmp;
			}
			ua.setAlarmType(startAlarmType+endAlarmType);
			
			userAlarmDAO.save(ua);
			
			try {
				schedulerRemoteService.addTrigger(isstart, dc.getUserId(), blogId, isstart?dc.getStartTime():dc.getEndTime(), dc.getSubject(), dc.getBlog());
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}else{
			String alarmWhen = ua.getAlarmWhen();
			String alarmType = ua.getAlarmType();
			if(isstart){
				alarmWhen = "1"+alarmWhen.substring(1, 2);
				if(use.equalsIgnoreCase("email")){
					alarmType = (value?"1":"0")+alarmType.substring(1, 6);
				}else if(use.equalsIgnoreCase("sms")){
					alarmType = alarmType.substring(0, 1)+(value?"1":"0")+alarmType.substring(2, 6);
				}else if(use.equalsIgnoreCase("call")){
					alarmType = alarmType.substring(0, 2)+(value?"1":"0")+alarmType.substring(3, 6);
				}
			}else{
				alarmWhen = alarmWhen.substring(0, 1)+"1";
				if(use.equalsIgnoreCase("email")){
					alarmType = alarmType.substring(0, 3)+(value?"1":"0")+alarmType.substring(4, 6);
				}else if(use.equalsIgnoreCase("sms")){
					alarmType = alarmType.substring(0, 4)+(value?"1":"0")+alarmType.substring(5, 6);
				}else if(use.equalsIgnoreCase("call")){
					alarmType = alarmType.substring(0, 5)+(value?"1":"0");
				}
			}
			ua.setAlarmType(alarmType);
			if(alarmType.equalsIgnoreCase("000000")){
				alarmWhen = "00";
			}else if(alarmType.startsWith("000")){
				alarmWhen = "0"+alarmWhen.substring(1, 2);
			}else if(alarmType.endsWith("000")){
				alarmWhen = alarmWhen.substring(0, 1)+"0";
			}
			ua.setAlarmWhen(alarmWhen);
			if(ua.getAlarmWhen().equalsIgnoreCase("00") && ua.getAlarmType().equalsIgnoreCase("000000")){
				userAlarmDAO.delete(ua.getId());
			}else{
				userAlarmDAO.save(ua);
			}
			
		}
		return ua.getAlarmWhen();
	}*/
	
	@Transactional
	public List<DaylogCalendar> listDaylogsByRange(String userId, Date startTime, Date endTime) {
		return this.dlCalendarDao.listDaylogsByRange(userId,startTime, endTime);
	}
	
	@Transactional
	public DaylogCalendar findById(String id) {
		return dlCalendarDao.findById(id);
	}
	
	public List<DaylogCalendar> findByUserIdAndIsDraft(String memberId, boolean isDraft, Pageable pageable){
		Member member = memberDao.findById(memberId);
		return dlCalendarDao.findByMemberAndIsDraft(member, isDraft, pageable);
	}
	
	@Transactional
	public void deleteById(String id) {/*
		DaylogCalendar dc = dlCalendarDao.findById(id);
		String userId = dc.getUserId();
		String blogId = dc.getId();
		
		try {
			schedulerRemoteService.deleteJob(userId, blogId);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		userAlarmDAO.deleteByBlogId(id);
		
		dlCalendarDAO.delete(id);
	*/}
	
	public boolean exists(String id){
		return dlCalendarDao.exists(id);
	}
	
	
	public int countByMemberId(String userId, boolean isDraft){
		return (int) dlCalendarDao.countByMemberId(userId, isDraft);
	}
	
}