package com.puma.plugin.daylog.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puma.common.BusinessHandler;
import com.puma.common.ResponseMessage;
import com.puma.core.base.BaseController;
import com.puma.core.domain.Member;
import com.puma.core.security.SecurityUtils;
import com.puma.plugin.daylog.CalendarMessage;
import com.puma.plugin.daylog.domain.DaylogCalendar;
import com.puma.plugin.daylog.service.DaylogCalendarService;
import com.puma.util.DateUtils;


@Controller
@RequestMapping("/daylogcalendar")
public class DaylogCalendarController extends BaseController{
	
	@Autowired
	private DaylogCalendarService daylogCalendarService;
	
	@RequestMapping("/daylistview.html")
	public ModelAndView calendarListView(@RequestParam(value="showdate",required=true)final String showdate, 
			  @RequestParam(value="viewtype",required=true)final String viewtype,
			  @RequestParam(value="timezone",required=true)final String timezone){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("daylog/listview");
		return mv;
	}
	
	@RequestMapping("quickadd.do")
	public @ResponseBody ResponseMessage quickAddNew(final DaylogCalendar daylogCalendar) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				/*return daylogCalendarService.save(daylogCalendar, null, true);*/
				return "";
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("quickupdate.do")
	public @ResponseBody ResponseMessage quickUpdate(final DaylogCalendar daylogCalendar) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				return "";
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("quickremove.do")
	public @ResponseBody ResponseMessage quickRemove(@RequestParam(value="id",required=true)final String id) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				daylogCalendarService.deleteById(id);
				return id;
			}
		}, logger);
		return response;
	}
	
	@RequestMapping("unreadcount.do")
	public @ResponseBody ResponseMessage countUnread() {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				Member member = SecurityUtils.getAuthedMember();
				String memberId = member.getId();
				return 0;
			}
		}, logger);
		return response;
	}
	
	/*@RequestMapping("getunread.do")
	public ModelAndView getUnreadContent(@RequestParam(value="start",required=false)Integer first,
			@RequestParam(value="count",required=false)Integer numPerPage){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("module/unreaddaylogs");
		Sort sort = new Sort(Direction.DESC, "createDate");
		PageRequest pageable=new PageRequest(first,numPerPage, sort);
		
		List<DaylogCalendarVO> dcList = daylogCalendarService.readAllBlogWithAlarm("readunread", pageable);
		mv.addObject("daylogCalendars", dcList);
		mv.addObject("daylogUser", Security.getAuthedDaylogUser());
		return mv;
	}*/
	
	@RequestMapping("list.do")
	public @ResponseBody CalendarMessage list(@RequestParam(value="showdate",required=true)final String showdate, 
											  @RequestParam(value="viewtype",required=true)final String viewtype,
											  @RequestParam(value="timezone",required=true)final String timezone) {
		
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		final DateFormat fmt3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date showtime = null;
		Date sday = null;
		Date eday = null;
		try {
			showtime = fmt.parse(showdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(viewtype.equalsIgnoreCase("day")||viewtype.equalsIgnoreCase("daylist")){
			try {
				sday = fmt2.parse(showdate + " 00:00");
				eday = fmt2.parse(showdate + " 23:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if(viewtype.equalsIgnoreCase("week")||viewtype.equalsIgnoreCase("weeklist")){
			sday = DateUtils.getFirstDayOfWeek(showtime);
			eday = DateUtils.getLastDayOfWeek(showtime);
		}else if(viewtype.equalsIgnoreCase("month")||viewtype.equalsIgnoreCase("monthlist")){
			sday = DateUtils.getFirstDayOfMonth(showtime);
			eday = DateUtils.getLastDayOfMonth(showtime);
		}
		final Date startDate = sday;
		final Date endDate = eday;
		
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				List<DaylogCalendar> daylogCalendars = daylogCalendarService.listDaylogsByRange(SecurityUtils.getAuthedMember().getId(), startDate, endDate);
				List<Object[]> list = new ArrayList<Object[]>();
				for(DaylogCalendar dc: daylogCalendars){
					String[] tmp = {
							dc.getId(),//0 id
							dc.getSubject(),//1 subject
							fmt3.format(dc.getStartTime()),//2start time
							fmt3.format(dc.getEndTime()),//3end time
							dc.getIsAllDayEvent()?"1":"0",//4 is all day event
							DateUtils.isInSameDay(dc.getStartTime(), dc.getEndTime())?"0":"1", // 5 crossday
							dc.getRecurringRule(),//6 repeat or not 0 for not /1 for repeat
							dc.getColor(),//7 theme color
							"1", //8 draggable option.enableDrag && e.event[8] == 1
							dc.getLocation(),//9 for location
							"attendee1, attendee2"//10 for attendees string
					};
					list.add(tmp);
				}
				return list;
			}
		}, logger);
		CalendarMessage cm = new CalendarMessage();
		cm.setEvents(response.getMessage());
		cm.setStart(fmt2.format(sday));
		cm.setEnd(fmt2.format(eday));
		if(response.getStatus().equalsIgnoreCase(ResponseMessage.ERROR)){
			cm.setError((String) response.getMessage());
		}else{
			cm.setError(null);
		}
		cm.setIssort(true);
		return cm;
	}
}