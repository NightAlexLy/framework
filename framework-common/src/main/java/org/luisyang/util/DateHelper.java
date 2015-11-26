package org.luisyang.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	public static final long SECODE_IN_MILLISECONDS = 1000L;
	public static final long MINUTE_IN_MILLISECONDS = 60000L;
	public static final long HOUR_IN_MILLISECONDS = 3600000L;
	public static final long DAY_IN_MILLISECONDS = 86400000L;
	public static final long WEEK_IN_MILLISECONDS = 604800000L;
	private static final int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * 比较两个日期<br/>
	 * 	date1 < date2 true
	 * @param date1 
	 * @param date2
	 * @return
	 */
	public static final boolean beforeDate(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getDate(calendar, date1);
		long time2 = getDate(calendar, date2);
		return time1 < time2;
	}

	/**
	 * 比较两个日期<br/>
	 * 	date1 <= date2 true
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean beforeOrMatchDate(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getDate(calendar, date1);
		long time2 = getDate(calendar, date2);
		return time1 <= time2;
	}

	/**
	 * 比较时间 忽略毫秒
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean beforeDateTime(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getDateTime(calendar, date1);
		long time2 = getDateTime(calendar, date2);
		return time1 < time2;
	}

	/**
	 * 比较时间<br/>
	 * 	忽略年/月/日/毫秒
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean beforeTime(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getTime(calendar, date1);
		long time2 = getTime(calendar, date2);
		return time1 < time2;
	}

	/**
	 * 获得时间<br/>
	 *  去掉时钟/分钟/秒钟/毫秒
	 * @param calendar
	 * @param date
	 * @return
	 */
	private static final long getDate(Calendar calendar, Date date) {
		calendar.setTimeInMillis(date.getTime());
		calendar.set(10, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获得时间 <br/>
	 *  去掉毫秒
	 * @param calendar
	 * @param date
	 * @return
	 */
	private static final long getDateTime(Calendar calendar, Date date) {
		calendar.setTimeInMillis(date.getTime());
		calendar.set(14, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 根据时间获得月份天数
	 * @param date
	 * @return
	 */
	public static final int getDaysOfMonth(Date date) {
		assert(date != null);
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(2);
		if ((month == 1) && (isLeepYear(calendar.get(1)))) {
			return 29;
		}
		return days[month];
	}

	/**
	 * 根据年/月获得月份天数
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static final int getDaysOfMonth(int year, int month) {
		if (year <= 0) {
			throw new IllegalArgumentException();
		}
		if ((month < 1) || (month > 12)) {
			throw new IllegalArgumentException();
		}
		if ((month == 1) && (isLeepYear(year))) {
			return 29;
		}
		return days[(month - 1)];
	}

	/**
	 * 获得当月的第一天
	 * @param date
	 * @return
	 */
	public static final int getFirstDayOfMonth(Date date) {
		assert(date != null);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(5, 1);
		return calendar.get(7);
	}

	/**
	 * 根据年/月获得这个一个月的第一天是星期几
	 * @param year
	 * @param month
	 * @return
	 */
	public static final int getFirstDayOfMonth(int year, int month) {
		if (year <= 0) {
			throw new IllegalArgumentException();
		}
		if ((month < 1) || (month > 12)) {
			throw new IllegalArgumentException();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, year);
		calendar.set(2, month - 1);
		calendar.set(5, 1);
		return calendar.get(7);
	}

	private static final long getTime(Calendar calendar, Date date) {
		calendar.setTimeInMillis(date.getTime());
		calendar.set(1, 1970);
		calendar.set(2, 0);
		calendar.set(5, 1);
		calendar.set(14, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 根据时间，获得是星期几
	 * @param calendar
	 * @param millis
	 * @param isSundayFirstDay
	 * @return
	 */
	public static final int getWeeksOfYear(Calendar calendar, long millis, boolean isSundayFirstDay) {
		calendar.setTimeInMillis(millis);
		calendar.set(2, 0);
		calendar.set(5, 1);
		int dayOfWeek = calendar.get(7);
		int weekEnd = isSundayFirstDay ? 1 : 7;
		while (dayOfWeek != weekEnd) {
			calendar.roll(5, true);
			dayOfWeek = calendar.get(7);
		}
		long start = calendar.getTimeInMillis();
		int week = 1;
		if (millis > start) {
			long weekMills = 604800000L;
			long dayMills = millis - start;
			week += (int) (dayMills / 604800000L);
			if (dayMills % 604800000L != 0L) {
				week++;
			}
		}
		return week;
	}

	public static final int getWeeksOfYear(Date date) {
		return getWeeksOfYear(Calendar.getInstance(), date.getTime(), false);
	}

	public static final int getWeeksOfYear(Date date, boolean isSundayFirstDay) {
		return getWeeksOfYear(Calendar.getInstance(), date.getTime(), isSundayFirstDay);
	}

	public static final int getWeeksOfYear(long millis) {
		return getWeeksOfYear(Calendar.getInstance(), millis, false);
	}

	public static final int getWeeksOfYear(long millis, boolean isSundayFirstDay) {
		return getWeeksOfYear(Calendar.getInstance(), millis, isSundayFirstDay);
	}

	/**
	 * 是否为闰年
	 * @param year
	 * @return
	 */
	public static final boolean isLeepYear(int year) {
		if (year <= 0) {
			throw new IllegalArgumentException();
		}
		return (((year & 0x3) == 0) && (year % 100 != 0)) || (year % 400 == 0);
	}

	/**
	 * 匹配两个时间
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean match(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return date1 == date2;
		}
		if (((date1 instanceof Timestamp)) || ((date2 instanceof Timestamp))) {
			return matchDateTime(date1, date2);
		}
		if (((date1 instanceof Time)) || ((date2 instanceof Time))) {
			return matchTime(date1, date2);
		}
		return matchDate(date1, date2);
	}

	/**
	 * 去掉时钟/分钟/秒钟/毫秒比较两个时间是否相等
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean matchDate(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return date1 == date2;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getDate(calendar, date1);
		long time2 = getDate(calendar, date2);
		return time1 == time2;
	}

	/**
	 * 去掉毫秒比较两个时间是否匹配
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean matchDateTime(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return date1 == date2;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getDateTime(calendar, date1);
		long time2 = getDateTime(calendar, date2);
		return time1 == time2;
	}

	/**
	 * 比较两个时间是否匹配
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final boolean matchTime(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return date1 == date2;
		}
		Calendar calendar = Calendar.getInstance();
		long time1 = getTime(calendar, date1);
		long time2 = getTime(calendar, date2);
		return time1 == time2;
	}

	/**
	 * 时间加指定的月数
	 * @param date
	 * @param monthes
	 * @return
	 */
	public static final long rollMonth(long date, int monthes) {
		if (monthes == 0) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		if (monthes > 0) {
			int month = calendar.get(2);
			for (int i = 0; i < monthes; i++) {
				if (month == 11) {
					calendar.roll(1, 1);
					month = -1;
				}
				month++;
			}
			calendar.roll(2, monthes);
			return calendar.getTimeInMillis();
		}
		for (int i = monthes; i < 0; i++) {
			if (calendar.get(2) == 0) {
				calendar.roll(1, -1);
			}
			calendar.roll(2, 1);
		}
		return calendar.getTimeInMillis();
	}

	/**
	 * 时间加指定的年数
	 * @param date 时间
	 * @param years 年数
	 * @return
	 */
	public static final long rollYear(long date, int years) {
		if (years == 0) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		calendar.roll(1, years);
		return calendar.getTimeInMillis();
	}
}