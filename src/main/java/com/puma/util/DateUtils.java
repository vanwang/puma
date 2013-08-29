package com.puma.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	/**
	 * 取得当前日期是多少周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = Calendar.getInstance();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

		return getWeekOfYear(c.getTime());
	}

	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 取得指定日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek() {
		/*Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();*/
		return getFirstDayOfWeek(new Date());
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek() {
		/*Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();*/
		return getLastDayOfWeek(new Date());
	}
	
	public static Date getFirstDayOfMonth()
	 {
		  Calendar c  =  Calendar.getInstance();
		  c.set(Calendar.HOUR, 0);
		  c.set(Calendar.MINUTE, 0);
		  c.set(Calendar.SECOND, 0);
		  c.set(Calendar.MILLISECOND, 0);
		  c.set( Calendar.DAY_OF_MONTH, 1 );
		  return c.getTime();
	 }
	
	 public static Date getFirstDayOfMonth( Date date )
	 {
		  Calendar c  =  Calendar.getInstance();
		  c.setTime( date );
		  c.set(Calendar.HOUR, 0);
		  c.set(Calendar.MINUTE, 0);
		  c.set(Calendar.SECOND, 0);
		  c.set(Calendar.MILLISECOND, 0);
		  c.set( Calendar.DAY_OF_MONTH, 1 );
		  return c.getTime();
	 }
	
	// 计算当月最后一天,返回字符串   
    public static Date getLastDayOfMonth(){     
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.HOUR, 23);
       lastDate.set(Calendar.MINUTE, 59);
       lastDate.set(Calendar.SECOND, 59);
       lastDate.set(Calendar.DATE,1);//设为当前月的1号   
       lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号   
       lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天   
       return lastDate.getTime();     
    }    
    
 // 计算当月最后一天,返回字符串   
    public static Date getLastDayOfMonth(Date date){     
       Calendar lastDate = Calendar.getInstance();   
       lastDate.setTime(date);
       lastDate.set(Calendar.HOUR, 23);
       lastDate.set(Calendar.MINUTE, 59);
       lastDate.set(Calendar.SECOND, 59);
       lastDate.set(Calendar.DATE,1);//设为当前月的1号   
       lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号   
       lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天   
       return lastDate.getTime();     
    }  
    
    public static boolean isInSameDay(Date dayone, Date daytwo){
    	DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    	return fmt.format(dayone).equalsIgnoreCase(fmt.format(daytwo));
    }
    
    public static void main(String[] args) {
	  System.out.println(DateUtils.getLastDayOfMonth());	
	}
	/*
	 * 
	 * 
	 * public static void main(String[] args) { int year = 2009; int week = 1;
	 * 
	 * // 以2006-01-02位例 Calendar c = Calendar.getInstance(); c.set(2009,
	 * Calendar.DECEMBER, 7); Date d = c.getTime();
	 * 
	 * System.out.println("current date = " + d);
	 * System.out.println("getWeekOfYear = " + getWeekOfYear(d));
	 * System.out.println("getMaxWeekNumOfYear = " + getMaxWeekNumOfYear(year));
	 * System.out.println("getFirstDayOfWeek = " + getFirstDayOfWeek(year,
	 * week)); System.out.println("getLastDayOfWeek = " + getLastDayOfWeek(year,
	 * week)); System.out.println ("getFirstDayOfWeek = " +
	 * getFirstDayOfWeek(d)); System.out.println("getLastDayOfWeek = " +
	 * getLastDayOfWeek(d)); System.out.println ("getFirstDayOfWeek = " +
	 * getFirstDayOfWeek()); System.out.println("getLastDayOfWeek = " +
	 * getLastDayOfWeek()); }
	 */
}