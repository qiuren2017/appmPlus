package com.cdfortis.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
	
	/**
	 * Data类型转换到String类型 
	 * @param time 
	 * @return yyy-mm-dd
	 */
	public static String toString(Date time){
		if(time == null){
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * Data类型转换为精确到时、分、秒的String类型
	 * @param time
	 * @return yyyy-MM-dd hh:mm:ss
	 */
	public static String toAccurateString(Date time){
		if(time == null){
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}
	/**
	 * 将String转换为Date 精确的时间，包含时、分、秒
	 * @param time
	 * @return
	 */
	public static Long toAccurateLongDate(String time){
		try {
			if(StringUtils.isBlank(time)){
				return null;
			}
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
