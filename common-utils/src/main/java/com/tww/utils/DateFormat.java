package com.tww.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式工具
 * @author zhaoql
 *
 */
public class DateFormat {

	public static String getYYMMDD(Date date){
		SimpleDateFormat f=new SimpleDateFormat("yyMMdd");
		return f.format(date);
	}
	
	public static String getBeforDayYYYYMMDD(Date date){
	    Calendar cal=Calendar.getInstance();
	    cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);	  
        Date yesterday=cal.getTime();
		SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
		return f.format(yesterday);
	}
	
	
	public static String getHHmmss(Date date){
		SimpleDateFormat f=new SimpleDateFormat("HHmmss");
		return f.format(date);
	}
	
	public static String getMMDD(Date date){
		SimpleDateFormat f=new SimpleDateFormat("MMdd");
		return f.format(date);
	}
	
	public static String getMMDDHHmmss(Date date){
		SimpleDateFormat f=new SimpleDateFormat("MMddHHmmss");
		return f.format(date);
	}
	
	public static int getNowHour(){
		 Calendar calendar=Calendar.getInstance();
	     Date date=new Date();
	     calendar.setTime(date);
	     int hour=calendar.get(Calendar.HOUR_OF_DAY);
	     return hour;
	}
	
	public static int getNowYear(){
		 Calendar calendar=Calendar.getInstance();
	     Date date=new Date();
	     calendar.setTime(date);
	     int hour=calendar.get(Calendar.YEAR);
	     return hour;
	}
	
	public static int getNowMonth(){
		 Calendar calendar=Calendar.getInstance();
	     Date date=new Date();
	     calendar.setTime(date);
	     int hour=calendar.get(Calendar.MONTH);
	     return hour;
	}
	
	public static Date getYYYYMMDD(String yyMMdd){
		SimpleDateFormat f1=new SimpleDateFormat("yyyy");
		String s=f1.format(new Date());
		yyMMdd=s.substring(0, 2)+yyMMdd;
	   
		Date d=null;
		SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
		try{
			d=f.parse(yyMMdd);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return d;
	}
	
	public static Date getDate(String yyyyMMdd){
		Date d=null;
		SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
		try{
			d=f.parse(yyyyMMdd);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return d;
	}
	
	
	public static Date getAfterDay(String yyyyMMdd){
		SimpleDateFormat f1=new SimpleDateFormat("yyyyMMdd");
	    try {
			Date date=f1.parse(yyyyMMdd);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
		    cal.add(Calendar.DAY_OF_MONTH, 1);	  
		    return cal.getTime();   
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	public static String getStringYYYYMMDD(Date date){
		SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
		String strDate = f.format(date);
		return strDate;
	}
	
	public static String getStringYYYYYearMMMonthDDDay(Date date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy年MM月dd");
		String strDate = f.format(date);
		return strDate;
	}
	
	public static Date getBefore3MonthDate(){
		Date dNow = new Date(); //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(calendar.MONTH, -3); //设置为前3月
		dBefore = calendar.getTime(); //得到前3月的时间
		return dBefore;
	}

	public static String getTime(Date date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = f.format(date);
		return strDate;
	}
	
	public static void main(String[] args){
		System.out.println(getStringYYYYMMDD(getBefore3MonthDate()));
	}
}
