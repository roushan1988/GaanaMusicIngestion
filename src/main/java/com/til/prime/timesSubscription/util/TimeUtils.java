package com.til.prime.timesSubscription.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by roushan on 29/10/15.
 */
public class TimeUtils {

	private static final TimeZone IST = TimeZone.getTimeZone("Asia/Calcutta");
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	private static final Long MILLIS_IN_A_DAY = 24*3600*1000l;

	enum DAY_OF_WEEK{
		SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

		private static final Map<Integer, DAY_OF_WEEK> dayMap = new HashMap<Integer, DAY_OF_WEEK>(){{
			for (DAY_OF_WEEK dow : DAY_OF_WEEK.values()) {
				put(dow.day, dow);
			}
		}};

		DAY_OF_WEEK(int day) {
			this.day = day;
		}

		private final int day;

		public static DAY_OF_WEEK getByDay(Integer day){
			return dayMap.get(day);
		}
	}

	public static final Long getMilliSecondsSinceMidnight(Long timeInMillis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(IST);
		calendar.setTimeInMillis(timeInMillis);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return timeInMillis - calendar.getTimeInMillis();
	}

	public static final Date getDayStartTime(Long daysAgo){
		Calendar dayStart = Calendar.getInstance();
		dayStart.add(Calendar.DATE, -(daysAgo.intValue()));
		dayStart.set(Calendar.HOUR_OF_DAY, 0);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return dayStart.getTime();
	}

	public static final Date getDayStartTime(Date date){
		Calendar dayStart = Calendar.getInstance();
		dayStart.setTime(date);
		dayStart.set(Calendar.HOUR_OF_DAY, 0);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return dayStart.getTime();
	}

	public static final Long getDayStartEpoch(Long timeInMillis){
		Calendar dayStart = Calendar.getInstance();
		dayStart.setTimeZone(IST);
		dayStart.setTimeInMillis(timeInMillis);
		dayStart.set(Calendar.HOUR_OF_DAY, 0);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return dayStart.getTimeInMillis();
	}

	public static final Long getDayEndEpoch(Long daysAgo){
		return getDayStartEpoch(daysAgo) + 86399999l;
	}

	public static final Long getMillisYearsAgo(Long yearsAgo){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, -(yearsAgo.intValue()));
		return now.getTimeInMillis();
	}

	public static final Date getNextDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	public static final Date addDaysInDate(Date date, int numberOfDays){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, numberOfDays);
		return calendar.getTime();
	}

	public static final Date addMillisInDate(Date date, int millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, millis);
		return calendar.getTime();
	}

	public static final Long getTime(String dateString, SimpleDateFormat sdf, TimeZone timeZone){
		sdf.setTimeZone(timeZone);
		try {
			Date date = sdf.parse(dateString);
			return date.getTime();
		} catch (Exception e) {
			throw new RuntimeException("Exception in Date parsing, date string: "+dateString);
		}
	}

	public static final Long getDifferenceInDays(Long time1, Long time2){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(IST);
		calendar.setTimeInMillis(time1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Long startTime = calendar.getTimeInMillis();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeZone(IST);
		calendar2.setTimeInMillis(time2);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		Long endTime = calendar2.getTimeInMillis();
		return Math.abs((endTime-startTime)/MILLIS_IN_A_DAY);
	}

	public static final Long getDifferenceInDays(Date start, Date end){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(IST);
		calendar.setTime(start);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Long startTime = calendar.getTimeInMillis();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeZone(IST);
		calendar2.setTime(end);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		Long endTime = calendar2.getTimeInMillis();
		return Math.abs((endTime-startTime)/MILLIS_IN_A_DAY);
	}

	public static final Long getDaysBeforeNow(Long time){
		return (new Date().getTime()-time)/MILLIS_IN_A_DAY;
	}
}
