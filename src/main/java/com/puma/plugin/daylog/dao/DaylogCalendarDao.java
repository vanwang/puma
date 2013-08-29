package com.puma.plugin.daylog.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.puma.core.base.BaseDao;
import com.puma.core.domain.Member;
import com.puma.plugin.daylog.domain.DaylogCalendar;

public interface DaylogCalendarDao extends BaseDao<DaylogCalendar, String>{
	
	@Query("select dc from DaylogCalendar dc where mid=:memberId  AND ((dc.startTime between :stime and :etime) OR (dc.endTime between :stime and :etime) OR (dc.endTime > :etime and dc.startTime < :stime))")
	public List<DaylogCalendar> listDaylogsByRange(@Param("memberId")String memberId,@Param("stime")Date startTime,@Param("etime")Date endTime);
	
	public DaylogCalendar findById(String id);
	
	public List<DaylogCalendar> findByMemberAndIsDraft(Member member, boolean isDraft, Pageable pageable);
	
	@Query("select COUNT(*) from DaylogCalendar where mid = ?1 and isDraft = ?2")
	public long countByMemberId(String memberId, boolean isDraft);
	
	public List<DaylogCalendar> findByIdIn(Collection<String> ids, Pageable pageable);
}