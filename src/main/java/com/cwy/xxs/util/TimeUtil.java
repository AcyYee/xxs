package com.cwy.xxs.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author acy
 * @version 2.1.0 by acy 20180321
 *
 * 统一的时间传入传出为String传入格式为"2018-01-01 00:00:00"
 * 添加格式时请添加一个私有的SimpleDateFormat和一个共有的静态变量并修改formatDate方法
 */

public class TimeUtil {

    private static final ThreadLocal<DateFormat>  SDF_COMMON_S = ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private static final ThreadLocal<DateFormat>  SDF_NONE_MS = ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyyMMddHHmmssS"));

    private static final ThreadLocal<DateFormat>  SDF_COMMON_DAY = ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyy-MM-dd"));

    private static final ThreadLocal<DateFormat>  SDF_COMMON_MS = ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyyMMddHHmmss"));

    private static final ThreadLocal<DateFormat>  SDF_NONE_S = ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));

    /**
     * 对比两个时间
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回前者是否小于后者
     */
    public static boolean comparingDate(String dateTime1, String dateTime2) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = SDF_COMMON_S.get().parse(dateTime1);
            date2 = SDF_COMMON_S.get().parse(dateTime2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return date1.getTime()<date2.getTime();
    }


    public enum FormatType {
        /**
         * 精确到秒
         */
        TO_SEC(1),
        /**
         * 精确到毫秒
         */
        TO_MS(2),
        /**
         * 精确到天
         */
        TO_DAY(3),
        /**
         * 精确到秒无符号
         */
        TO_SEC_NONE(4),
        /**
         *  精确到毫秒无符号
         */
        TO_MS_NONE(5);

        private int action;

        FormatType(int action){
            this.action = action;
        }

        public int getAction() {
            return action;
        }
    }

    /**
     * 内部方法
     *
     * @param type 类型
     * @param date 需要格式的时间
     * @return 返回格式后的String
     */
    public static String formatDate(int type, Date date) {
        switch (type) {
            case 1:
                return SDF_COMMON_S.get().format(date);
            case 2:
                return SDF_NONE_S.get().format(date);
            case 3:
                return SDF_COMMON_DAY.get().format(date);
            case 4:
                return SDF_NONE_MS.get().format(date);
            case 5:
                return SDF_COMMON_MS.get().format(date);
            default:
                return SDF_COMMON_S.get().format(date);
        }
    }

    public static String formatDate(FormatType type, Date date) {
        switch (type) {
            case TO_SEC:
                return SDF_COMMON_S.get().format(date);
            case TO_MS:
                return SDF_NONE_S.get().format(date);
            case TO_DAY:
                return SDF_COMMON_DAY.get().format(date);
            case TO_SEC_NONE:
                return SDF_NONE_MS.get().format(date);
            case TO_MS_NONE:
                return SDF_COMMON_MS.get().format(date);
            default:
                return SDF_COMMON_S.get().format(date);
        }
    }

    /**
     * 获取当前时间
     *
     * @param type TimeUtil的静态变量 详情见的注释
     */
    public static String getDateTime(int type) {
        Date date = new Date();
        return formatDate(type, date);

    }

    public static String getDateTime(FormatType type) {
        Date date = new Date();
        return formatDate(type, date);

    }

    /**
     * 获取当天的开始结束时间
     */
    public static String[] getTodayDateTime() {
        String[] dateTimes = new String[2];
        Date date = new Date();
        String temp = formatDate(3, date);
        dateTimes[0] = temp + " 00:00:00";
        dateTimes[1] = temp + " 23:59:59";
        System.out.println("当天的开始时间:" + dateTimes[0]);
        System.out.println("当天的结束时间:" + dateTimes[1]);
        return dateTimes;
    }

    /**
     * 获取的昨天的开始结束时间
     */
    public static String[] getYesterdayDateTime() {
        String[] dateTimes = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, -1);
        String temp = formatDate(3, calendar.getTime());
        dateTimes[0] = temp + " 00:00:00";
        dateTimes[1] = temp + " 23:59:59";
        return dateTimes;
    }

    /**
     * 获取上周的开始结束时间
     */
    public static String[] getLastWeekTime() {
        System.out.println("获取上周的开始结束时间");
        String[] dateTimes = new String[2];
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 0 - dayOfWeek;
        int offset2 = 6 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        dateTimes[0] = formatDate(3, calendar1.getTime());
        dateTimes[1] = formatDate(3, calendar2.getTime());
        return dateTimes;
    }

    /**
     * 获取的之前之后的日期
     *
     * @param dayCount 需要计算的天数 之前为- 之后为+
     * @param type     TimeUtil的静态变量 详情见的注释
     */
    public static String getBeforeAfterDay(int dayCount, int type) {
        System.out.println("获取的之前之后的日期");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +dayCount);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 获取当周的开始结束时间
     */
    public static String[] getThisWeek() {
        System.out.println("获取当周的开始结束时间");
        String[] dateTimes = new String[2];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        dateTimes[0] = formatDate(3, cal.getTime());
        cal.set(Calendar.DAY_OF_WEEK, 7);
        dateTimes[1] = formatDate(3, cal.getTime());
        return dateTimes;
    }

    /**
     * 获取上月的开始结束时间
     */
    public static String[] getLastMonthDateTime() {
        System.out.println("获取当周的开始结束时间");
        String[] dateTimes = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        dateTimes[0] = formatDate(3, calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        dateTimes[1] = formatDate(3, calendar.getTime());
        return dateTimes;
    }

    /**
     * 对时间添加减少月份
     *
     * @param dateTime 需要修改的时间
     * @param sumMonth 需要增加减少的月数
     * @param type     需要得到的格式 TimeUtil的静态变量 详情见的注释
     * @return 返回修改后的时间
     */
    public static String getDateAddMouth(String dateTime, Integer sumMonth, int type) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, +sumMonth);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 对时间添加减少分钟
     *
     * @param dateTime       需要修改的时间
     * @param addMinuteCount 需要增加减少的分钟
     * @param type           需要得到的格式 TimeUtil的静态变量 详情见的注释
     * @return 返回修改后的时间
     */
    public static String getDateAddMinute(String dateTime, int addMinuteCount, int type) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MINUTE, +addMinuteCount);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 对时间添加减少分钟
     *
     * @param dateTime       需要修改的时间
     * @param addMinuteCount 需要增加减少的分钟
     * @return 返回修改后的时间
     */
    public static Date getDateAddMinute(String dateTime, int addMinuteCount) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MINUTE, +addMinuteCount);
        return calendar.getTime();
    }

    /**
     * 对时间添加减少小时
     *
     * @param dateTime     需要修改的时间
     * @param addHourCount 需要增加减少的小时
     * @param type         需要得到的格式 TimeUtil的静态变量 详情见的注释
     * @return 返回修改后的时间
     */
    public static String getDateAddHour(String dateTime, int addHourCount, int type) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR, +addHourCount);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 对时间添加减少年数
     *
     * @param dateTime     需要修改的时间
     * @param addYearCount 需要增加减少的年数
     * @param type         需要得到的格式 TimeUtil的静态变量 详情见的注释
     * @return 返回修改后的时间
     */
    public static String getDateAddYear(String dateTime, int addYearCount, int type) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.YEAR, +addYearCount);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 对时间添加减少周数
     *
     * @param dateTime     需要修改的时间
     * @param addWeekCount 需要增加减少的周数
     * @param type         需要得到的格式 TimeUtil的静态变量 详情见的注释
     * @return 返回修改后的时间
     */
    public static String getDateAddWeek(String dateTime, int addWeekCount, int type) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.WEEK_OF_YEAR, +addWeekCount);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 对时间添加减少天数
     *
     * @param dateTime    需要修改的时间
     * @param addDayCount 需要增加减少的天数
     * @param type        需要得到的格式 TimeUtil的静态变量 详情见的注释
     * @return 返回修改后的时间
     */
    public static String getDateAddDay(String dateTime, int addDayCount, int type) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, +addDayCount);
        return formatDate(type, calendar.getTime());
    }

    /**
     * 对时间添加减少天数
     *
     * @param dateTime    需要修改的时间
     * @param addDayCount 需要增加减少的天数
     * @return 返回修改后的时间
     */
    public static Date getDateAddDay(String dateTime, int addDayCount) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(SDF_COMMON_S.get().parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, +addDayCount);
        return calendar.getTime();
    }

    /**
     * 获得本月第一天?
     */
    public static String getMonthOfFirst() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(3, c.getTime());
    }

    /**
     * 获得本月最后一天
     */
    public static String getMonthOfLast() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(3, ca.getTime());
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt 当前时间
     */
    public static int getWeekOfDate(Date dt) {
        Integer[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 1);
        return formatDate(3, c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 7);
        return formatDate(3, c.getTime());
    }

	/*public static void main(String[] args){
        Date date = new Date();
		String dateTime = SDF_COMMON_S.format(date);
		System.out.println(getDateAddDay(dateTime,-333,5));
		System.out.println(getDateAddWeek(dateTime,-60,5));
		System.out.println(getDateAddYear(dateTime,-31,5));
		System.out.println(getDateAddHour(dateTime,-333,5));
		System.out.println(getDateAddMinute(dateTime,-333,5));
		System.out.println(getDateAddMouth(dateTime,-333,5));
		System.out.println(getLastMonthDateTime());
		System.out.println(getThisWeek());
		System.out.println(getBeforeAfterDay(-11,5));
		System.out.println(getLastWeekTime());
		System.out.println(getYesterdayDateTime());
		System.out.println(getTodayDateTime());
		System.out.println(getDateTime(1));
		System.out.println(getDateTime(2));
		System.out.println(getDateTime(3));
		System.out.println(getDateTime(4));
		System.out.println(getDateTime(5));
	}*/

}
