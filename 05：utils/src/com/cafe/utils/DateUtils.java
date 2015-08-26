package com.cafe.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 * 时间转化工具类转化类
 */
public class DateUtils {

	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

	@SuppressLint("SimpleDateFormat")
	public static String format_yyyyMMdd_HHmmss(int timestamp)
	{
		SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
		return format.format(new Date(timestamp));

	}

	@SuppressLint("SimpleDateFormat")
	public static long parse_yyyyMMdd_HHmmss(String formatDate)
	{
		SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
		try {
			Date date = format.parse(formatDate);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return System.currentTimeMillis();
	}
	/**
	 * UTC时间转换成本地时间
	 * @param utcTime
	 * @param utcTimePatten
	 * @param localTimePatten
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String utc2Local(String utcTime, String utcTimePatten, 
			String localTimePatten) { 
		String localTime = null;
		try { 
			SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten); 
			utcFormater.setTimeZone(TimeZone.getTimeZone("UTC")); 
			Date gpsUTCDate = null; 

			gpsUTCDate = utcFormater.parse(utcTime); 

			SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten); 
			localFormater.setTimeZone(TimeZone.getDefault()); 
			localTime = localFormater.format(gpsUTCDate.getTime()); 
			return localTime;
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return utcTime; 
	}

	@SuppressLint("SimpleDateFormat")
	public static String compareCurrentTime(String utcTime){
		String result;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String utcTime2 = utc2Local(utcTime,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(utcTime2);
			Date d2 = new Date();
			long diff = (d2.getTime() - d1.getTime())/1000;
			long temp = 0;
			result = null;
			if (diff < 60) {
				result = "刚刚";
			}
			else if((temp=(diff/60)) <60){
				result = temp+"分前";
			}

			else if((temp=(temp/60)) <24){
				result = temp+"小时前";
			}

			else if((temp=(temp/24)) <8){
				result = temp+"天前";
			}
			else{
				return utcTime2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return utcTime;
		}
		return result;
	}
	
	/**
	 * 得到当前日期是星期几。
	 * 
	 * @return 当为周日时，返回0，当为周一至周六时，则返回对应的1-6。
	 */
	public static final int getCurrentDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
	}
}
