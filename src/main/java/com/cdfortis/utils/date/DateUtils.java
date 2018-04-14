package com.cdfortis.utils.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @title 日期工具类
 * @Description: 用于字符串日期和Date日期的转换和日期的增加、减少和日期的格式化
 * @CreateDate: 2016-9-22下午5:41:39
 */
public class DateUtils {

	// 私有化构造器，防止工具方法被创建对象
	private DateUtils() {
		throw new AssertionError("[Tool utils can't be instantiation]");
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return "";
		if (pattern == null)
			pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return (sdf.format(date));
	}

	/**
	 * 
	 * @Title:格式化日期为yyyy-MM-dd HH:mm:ss
	 * @Description: 通过 SimpleDateFormat 将Date类型格式化为String类型的时间
	 * @param Date
	 *            date
	 * @return String date
	 * @CreateDate: 2017-1-17上午9:23:42
	 */
	public static String formatDateTime(Date date) {
		return (formatDate(date, "yyyy-MM-dd HH:mm:ss"));
	}

	public static String formatDateTime() {
		return (formatDate(now(), "yyyy-MM-dd HH:mm:ss"));
	}

	public static String formatDateTime2() {
		return (formatDate(now(), "yyyyMMddHHmmss"));
	}

	public static String formatDate(Date date) {
		return (formatDate(date, "yyyy-MM-dd"));
	}

	public static String formatDate() {
		return (formatDate(now(), "yyyy-MM-dd"));
	}

	/**
	 * 
	 * @return
	 */
	public static String formatDate2() {
		return (formatDate(now(), "yyyyMMdd"));
	}

	public static String formatTime(Date date) {
		return (formatDate(date, "HH:mm:ss"));
	}

	public static String formatTime() {
		return (formatDate(now(), "HH:mm:ss"));
	}

	public static String formatTime2() {
		return (formatDate(now(), "HHmmss"));
	}

	public static Date now() {
		return (new Date());
	}

	public static Date parseDateTime(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isBlank(datetime)) {
			return null;
		} else {
			try {
				return formatter.parse(datetime);
			} catch (ParseException e) {
				e.printStackTrace();
				return parseDate(datetime);
			}
		}
	}

	public static String parseDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null) {
			return formatter.format(new Date());
		} else {
			return formatter.format(date);
		}
	}

	public static Date parseDate(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		} else {
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * @Title: parseSimpleDate
	 * @Description: 将只有月份的时间格式转化为Date类型
	 * @CreateDate: 2017年7月25日上午11:14:13
	 * @author: zhangn
	 */
	public static Date parseSimpleDate(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		} else {
			try {
				return new SimpleDateFormat("yyyy-MM").parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 
	 * @Title: parseDate2   
	 * @Description:  yyyyMMdd
	 * @param   
	 * @return Date
	 * @throws
	 */
	public static Date parseDate2(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		if (StringUtils.isBlank(date)) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 
	 * @Title: parseDate   
	 * @Description: yyyy-MM-dd
	 * @param   
	 * @return Date
	 * @throws
	 */
	public static Date parseDate(Date datetime) {
		if (datetime == null) {
			return null;
		} else {
			try {
				SimpleDateFormat simpleFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				return simpleFormat.parse(simpleFormat.format(datetime));
			} catch (ParseException e) {
				return null;
			}
		}
	}

	public static String formatDate(Object o) {
		if (o == null)
			return "";
		if (o.getClass() == String.class)
			return formatDate(o);
		else if (o.getClass() == Date.class)
			return formatDate((Date) o);
		else if (o.getClass() == Timestamp.class) {
			return formatDate(new Date(((Timestamp) o).getTime()));
		} else
			return o.toString();
	}

	public static String formatDateTime(Object o) {
		if (o.getClass() == String.class)
			return formatDateTime(o);
		else if (o.getClass() == Date.class)
			return formatDateTime((Date) o);
		else if (o.getClass() == Timestamp.class) {
			return formatDateTime(new Date(((Timestamp) o).getTime()));
		} else
			return o.toString();
	}

	/**
	 * 给时间加上或减去指定毫秒，秒，分，时，天、月或年等，返回变动后的时间
	 * 
	 * @param date
	 *            要加减前的时间，如果不传，则为当前日期
	 * @param field
	 *            时间域，有Calendar.MILLISECOND,Calendar.SECOND,Calendar.MINUTE,<br>
	 *            Calendar.HOUR,Calendar.DATE, Calendar.MONTH,Calendar.YEAR
	 * @param amount
	 *            按指定时间域加减的时间数量，正数为加，负数为减。
	 * @return 变动后的时间
	 */
	public static Date add(Date date, int field, int amount) {
		if (date == null) {
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);

		return cal.getTime();
	}

	public static Date addMilliSecond(Date date, int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	public static Date addSecond(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	public static Date addMiunte(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	public static Date addHour(Date date, int amount) {
		return add(date, Calendar.HOUR, amount);
	}

	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DATE, amount);
	}

	public static Date addMonth(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date addYear(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	public static Date getDate() {
		return parseDate(formatDate());
	}

	public static String getDatePath(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		StringBuffer path = new StringBuffer();
		path.append("/");
		path.append(c.get(Calendar.YEAR));
		path.append("/");
		path.append(c.get(Calendar.MONTH) + 1);
		path.append("/");
		path.append(c.get(Calendar.DATE));
		return path.toString();
	}

	public static Date getDateTime() {
		return parseDateTime(formatDateTime());
	}

	public static Date getDate(String dateStr) {
		Date temp1 = null;
		if (dateStr == null)
			return null;
		if (dateStr.equals(""))
			return null;
		SimpleDateFormat formatter = null;
		try {
			if (dateStr.indexOf(" ") != -1) {
				String[] aa = StringUtils.split(dateStr, ":");
				if (aa.length == 3) {
					formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				} else if (aa.length == 2) {
					formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				} else {
					formatter = new SimpleDateFormat("yyyy-MM-dd HH");
				}
			} else {
				if (dateStr.indexOf("-") == -1) {
					formatter = new SimpleDateFormat("yyyyMMdd");
				} else {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
				}
			}
			temp1 = formatter.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp1;
	}

	public static Date getDate(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Date) {
			return (Date) o;
		} else if (o instanceof String) {
			return getDate(String.valueOf(o));
		} else if (o instanceof java.sql.Timestamp) {
			return new Date(((java.sql.Timestamp) o).getTime());
		} else if (o instanceof java.sql.Date) {
			return new Date(((java.sql.Date) o).getTime());
		} else {
			return null;
		}
	}

	public static String dateToStr(Date date, String format) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		}
	}

	public static String dateToStr(Date date) {
		return dateToStr(date, "yyyy-MM-dd");
	}

	public static String dateToCNStr(Date date) {
		return dateToStr(date, "yyyy年MM月dd日");
	}

	public static String dateToStrYYYMMDD(Date date) {
		return dateToStr(date, "yyyyMMdd");
	}

	@SuppressWarnings("deprecation")
	public static String getDateStrByTime(long time) {
		Date date = new Date(time);
		return date.toLocaleString().substring(0,
				date.toLocaleString().indexOf(" "));
	}

	public static Timestamp getNewTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	// 返回传入日期本月第一天的0点0分0秒0毫秒
	public static Date firstOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 返回传入日期的本月最后一天的59分59秒
	public static Date endOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	// 获取本周一的0点0分0秒
	public static Date firstOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 获取本周末的最后一秒
	public static Date endOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	// 将传入日期修改为当天的0点0分0秒
	public static Date firstOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 将传入的值修改为当天的最后一秒
	public static Date endOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// cal.set(Calendar.MILLISECOND,999);
		return cal.getTime();
	}

	// 获取指定日期的星期一
	public static String firstOfDate(String date) throws ParseException {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date time = simpleFormat.parse(date);
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		return simpleFormat.format(cal.getTime());
	}

	/**
	 * 
	 * @Title: getWeekOneDay   
	 * @Description: 获取指定日期的星期一
	 * @param   
	 * @return Date
	 * @throws
	 */
	public static Date getWeekOneDay(Date date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		return cal.getTime();
	}

	// 获取指定日期的星期天
	public static String endOfDate(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date time = simpleFormat.parse(date);
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.add(Calendar.DATE, 6);
		return simpleFormat.format(cal.getTime());
	}

	/**
	 * <p>
	 * 
	 * @Title: 获取输入日期的所在的天
	 *         </p>
	 *         <p>
	 * @Description: </p>
	 *               <p>
	 * @CreateDate: 2016-12-22上午10:08:00
	 *              </p>
	 */
	public static Integer getDay(Date date) {
		if (date == null) {
			return -1;
		}
		return Integer.parseInt(new SimpleDateFormat("dd").format(date));
	}

	/**
	 * <p>
	 * 
	 * @Title: 获取输入日期所在的天的月份
	 *         </p>
	 *         <p>
	 * @Description: </p>
	 *               <p>
	 * @CreateDate: 2016-12-22上午10:11:37
	 *              </p>
	 */
	public static Integer getMouth(Date date) {
		if (date == null) {
			return -1;
		}
		return Integer.parseInt(new SimpleDateFormat("MM").format(date));
	}

	/**
	 * <p>
	 * 
	 * @Title: 获取输入日期的年份
	 *         </p>
	 *         <p>
	 * @Description: </p>
	 *               <p>
	 * @CreateDate: 2016-12-22上午10:33:34
	 *              </p>
	 */
	public static Integer getYear(Date date) {
		if (date == null) {
			return -1;
		}
		return Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
	}

	/**
	 * 
	 * @Title: listDateStrInTwoTime
	 * @Description: TODO(查询时间范围内的每一天)
	 * @param dBegin
	 *            2017-01-01
	 * @param dEnd
	 *            2017-02-02
	 * @return List<String> [2017-01-01,2017-01-02,...,2017-02-02]
	 * @throws
	 */
	public static List<String> listDateStrInTwoTime(Date dBegin, Date dEnd) {
		List<String> lDate = new ArrayList<String>();
		lDate.add(DateUtils.formatDate(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(formatDate(calBegin.getTime()));
		}
		return lDate;
	}

	/**
	 * 
	 * @Title: listDateStr_S26_E25
	 * @Description: TODO(获取上月26号到本月25号的时间列表)
	 * @param 2018-01-02
	 * @return List<String> [2017-12-26,2017-12-27,...,2018-01-25]
	 * @throws
	 */
	public static List<String> listDateStr_S26_E25(String dateTime) {
		if (StringUtils.isBlank(dateTime)) {
			dateTime = formatDate();
		}
		Date input = getDate(dateTime);
		String startTime = getYear(input) + "-" + (getMouth(input) - 1) + "-26";
		String endTime = getYear(input) + "-" + getMouth(input) + "-25";
		return listDateStrInTwoTime(getDate(startTime), getDate(endTime));
	}

	/**
	 * 
	 * @Title: getWeekOfDate
	 * @Description: TODO(获取当前日期是星期几)
	 * @param 2018-01-24
	 * @return String 星期三
	 * @throws
	 */
	public static String getWeekOfDate(String dateTime) {
		if (StringUtils.isBlank(dateTime)) {
			dateTime = formatDate();
		}
		Date input = getDate(dateTime);
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(input);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

}
