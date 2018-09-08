package com.freshman.webapi.util.dateUtil;

import com.freshman.webapi.util.convertutil.TConverter;
import com.freshman.webapi.util.stringutil.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    //默认格式
    private static String datePattern = "yyyy-MM-dd";
    private static String datePattern2 = "yyyy-MM";
    private static String timePattern = datePattern + " HH:mm:ss";

    /**
     * 日期转为字符串
     *
     * @param aDate
     * @return
     */
    public static final String date2Str(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(timePattern);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * 日期转为字符串
     *
     * @param pattern
     * @param aDate
     * @return
     */
    public static final String date2Str(String pattern, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(pattern);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * 字符串转为日期，失败返回null
     *
     * @param pattern
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static final Date str2Date(String pattern, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(pattern);

        try {
            if (StringHelper.isEmpty(strDate))
                return null;
            date = df.parse(strDate);
        } catch (Exception pe) {

        }
        if (date == null)
            date = DateHelper.str2Date02(timePattern, strDate);
        if (date == null)
            date = DateHelper.str2Date02(datePattern, strDate);
        if (date == null)
            date = DateHelper.str2Date02(datePattern2, strDate);
        if (date == null)
            date = new Date(TConverter.ObjectToLong(strDate));
        return (date);
    }

    public static final Date str2Date02(String pattern, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(pattern);

        try {
            if (StringHelper.isEmpty(strDate))
                return null;
            date = df.parse(strDate);
        } catch (Exception pe) {

        }
        return (date);
    }

    /**
     * 字符串转为日期，失败返回null
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static final Date str2Date(String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(timePattern);

        try {
            if (StringUtils.isEmpty(strDate))
                return null;
            date = df.parse(strDate);
        } catch (Exception pe) {

        }
        if (date == null)
            date = DateHelper.str2Date02(datePattern, strDate);
        if (date == null)
            date = DateHelper.str2Date02(datePattern2, strDate);
        if (date == null)
            date = new Date(TConverter.ObjectToLong(strDate));
        return (date);
    }

    /**
     * 获取当前时
     *
     * @return
     */
    public static String getTimeNow() {
        Date nowTime = new Date();
        return date2Str(nowTime);
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getTimeNow(String pattern) {
        Date nowTime = new Date();
        return date2Str(pattern, nowTime);
    }

    /**
     * 增加日
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    /**
     * 添加年
     *
     * @param date
     * @param year
     * @return
     */
    public static Date addYear(Date date, int year) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.YEAR, year);
        return now.getTime();
    }

    /**
     * 获取当天开始时间
     *
     * @param date
     * @return
     */
    public static Date getDayStartTime(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取当天结束时间
     *
     * @param date
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /***
     * 获取当前时间的前部分
     * @return
     */
    public static String getStringDateShort(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /***
     * 获取当前时间的后部分
     * @return
     */
    public static String getTimeShort(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间戳
     *
     * @param date
     * @param expireTime 添加的额外的时间戳，单位秒
     * @return
     */
    public static String getTimestamp(Date date, int expireTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(timePattern);
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.SECOND, expireTime);
        long ts = now.getTime().getTime();
        return String.valueOf(ts);
    }

    /**
     * 添加秒
     *
     * @param date
     * @param expireTime
     * @return
     */
    public static Date addSecond(Date date, int expireTime) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.SECOND, expireTime);
        return now.getTime();
    }

    /**
     * 增加分
     *
     * @param date
     * @param expireTime
     * @return
     */
    public static Date addMinute(Date date, int expireTime) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MINUTE, expireTime);
        return now.getTime();
    }

    /**
     * 增加月
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addMonth(Date date, int month) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MONTH, month);
        return now.getTime();
    }

    /**
     * 获取月初
     *
     * @param date
     * @return
     */
    public static Date getMonthBeginTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    /**
     * 获取传入日期所在月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取传入日期所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取传入日期所在年的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取传入日期所在年的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取日开始时间
     *
     * @param date
     * @return
     */
    public static Date getDayBeginTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期的天
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
