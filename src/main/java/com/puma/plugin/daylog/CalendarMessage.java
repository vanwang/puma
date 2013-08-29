package com.puma.plugin.daylog;

import java.io.Serializable;

public class CalendarMessage implements Serializable {

	private static final long serialVersionUID = 5782767539463119021L;
	
	private String start = "";
	private String end = "";
	private String error = "";
	private boolean issort = true;
	private Object events;
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean isIssort() {
		return issort;
	}
	public void setIssort(boolean issort) {
		this.issort = issort;
	}
	public Object getEvents() {
		return events;
	}
	public void setEvents(Object events) {
		this.events = events;
	}
	
	
}
