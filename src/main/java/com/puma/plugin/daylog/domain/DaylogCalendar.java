package com.puma.plugin.daylog.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.puma.core.base.BaseEntity;
import com.puma.core.domain.Member;

@Entity
@Table(name = "dl_calendar")
@JsonIgnoreProperties({"member"})
public class DaylogCalendar extends BaseEntity{ 

	private static final long serialVersionUID = 1L;

	@Column(name = "subject", length =1000)
	private String subject;
	@Column(name = "location", length = 200)
	private String location;
	@Column(name = "description", length = 255)
	private String description;
	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime ;
	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime ;
	@Column(name = "is_all_day_event", length = 1)
	private Boolean isAllDayEvent;
	@Column(name = "is_public", length = 1)
	private Boolean isPublic;
	@Column(name = "is_draft", length = 1)
	private Boolean isDraft;
	@Column(name = "color", length = 200)
	private String color;
	@Column(name = "recurring_rule", length = 500)
	private String recurringRule;
	@Column(name = "blog")
	private String blog;
	@Column(name = "tag", length = 256)
	private String tag;
//	@Column(name = "mid", length = 32, nullable = false)
//	private String memberId;// ID
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mid", referencedColumnName = "id")
	private Member member;
	
	@Transient
	private String stpartdate;
	@Transient
	private String stparttime;
	@Transient
	private String etpartdate;
	@Transient
	private String etparttime;
	
	
	public DaylogCalendar() {
		super();
		this.isDraft = false;
		this.isAllDayEvent = false;
		this.isPublic = false;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getIsAllDayEvent() {
		if(null == isAllDayEvent)
			return false;
		else
			return isAllDayEvent;
	}

	public void setIsAllDayEvent(Boolean isAllDayEvent) {
		this.isAllDayEvent = isAllDayEvent;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRecurringRule() {
		return recurringRule;
	}

	public void setRecurringRule(String recurringRule) {
		this.recurringRule = recurringRule;
	}

	public String getStpartdate() {
		return stpartdate;
	}

	public void setStpartdate(String stpartdate) {
		this.stpartdate = stpartdate;
	}

	public String getStparttime() {
		return stparttime;
	}

	public void setStparttime(String stparttime) {
		this.stparttime = stparttime;
	}

	public String getEtpartdate() {
		return etpartdate;
	}

	public void setEtpartdate(String etpartdate) {
		this.etpartdate = etpartdate;
	}

	public String getEtparttime() {
		return etparttime;
	}

	public void setEtparttime(String etparttime) {
		this.etparttime = etparttime;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	/*public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}*/

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}