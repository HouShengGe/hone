package com.mc.app.hotel.common.facealignment.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    public static String getCurrentFormateTime() {
        return format24TimeWithoutSecond(getCurrentTime());
    }
    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    public static String getNextDay() {

        return getCurrentYear()+"-"+getCurrentMonth()+"-"+getCurrentDay()+" "+"13:00";
    }

    public static String getCurrentTimeString() {
        return format24Time(getCurrentTime());
    }

    /**
     * 格式化12小时制<br>
     * 格式：yyyy-MM-dd hh-MM-ss
     *
     * @param time 时间
     * @return
     */
    public static String format12Time(long time) {
        return format(time, "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 格式化24小时制<br>
     * 格式：yyyy-MM-dd HH-MM-ss
     *
     * @param time 时间
     * @return
     */
    public static String format24Time(long time) {
        return format(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format24TimeWithoutSecond(long time) {
        return format(time, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化日期<br>
     * 格式：yyyy-MM-dd
     *
     * @param time 时间
     * @return
     */
    public static String formatDate(long time) {
        return format(time, "yyyy-MM-dd");
    }

    /**
     * 格式化日期<br>
     * 格式：yyyy-MM-dd
     *
     * @param time 时间
     * @return
     */
    public static String formatDateCh(long time) {
        return format(time, "yyyy年MM月dd日");
    }

    /**
     * 格式化时间<br>
     * 格式：yyyy-MM-dd
     *
     * @param time 时间
     * @return
     */
    public static String formatTime(long time) {
        return format(time, "HH:mm:ss");
    }

    /**
     * 格式化时间<br>
     * 格式：yyyy-MM-dd
     *
     * @param time 时间
     * @return
     */
    public static String formatTimes(long time) {
        return format(time, "HH:mm");
    }

    /**
     * 格式化时间,自定义标签
     *
     * @param time    时间
     * @param pattern 格式化时间用的标签
     * @return
     */
    public static String format(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }

    /**
     * 获取当前天
     *
     * @return 天
     */
    @SuppressWarnings("static-access")
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月
     *
     * @return 月
     */
    @SuppressWarnings("static-access")
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(calendar.MONTH) + 1;
    }

    /**
     * 获取当前年
     *
     * @return 年
     */
    @SuppressWarnings("static-access")
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(calendar.YEAR);
    }

    /**
     * 秒转换成分格式
     *
     * @param
     * @return
     */
    public static String secondTominute(int ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        long second = (ms - hour * hh - minute * mi) / ss;
        String strMinute = minute == 0 ? "00" : minute < 10 ? "0" + minute : minute + "";
        strMinute = strMinute + ":";
        String strSecond = second < 10 ? "0" + second : second + "";
        return strMinute + strSecond;
    }

    /**
     * 获取当前日期 （今天，昨天，yyyy-MM-dd）
     *
     * @param time
     * @param
     * @return
     */
    public static String getDate(long time) {
        String date = "";
        String day = formatDate(time);
        String[] ymd = day.split("-");
        if (ymd.length == 3 && ymd[0].equals(getCurrentYear() + "") && ymd[1].equals(getCurrentMonth() + "")) {
            if (ymd[2].equals(getCurrentDay() + "")) {
                date = "今天";
            } else if ((getCurrentDay() - Integer.parseInt(ymd[2])) == 1) {
                date = "昨天";
            } else {
                date = day;
            }
        } else {
            date = day;
        }
        return date;
    }

    /**
     * 时间差计算
     *
     * @param begin
     * @param end
     * @return long[day, hour, min, s, ms]
     */
    public static long[] twoTimeLimit(Date begin, Date end) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long between = 0;
        try {
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);

        return new long[]{day, hour, min, s, ms};
    }

    /**
     * 标准时间格式 yyyy/MM/dd HH:mm:ss
     *
     * @param string
     * @return 时间戳
     */
    public static long string2timestamp(String string) {
        String dateStr = string;
        Date date = new Date();
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 自定义时间格式
     *
     * @param string
     * @return 时间戳
     */
    public static long string2timestamp(String string, String timeFormat) {
        String dateStr = string;
        Date date = new Date();
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat(timeFormat);
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 年月日获取时间戳
     *
     * @param year
     * @param month
     * @param day
     * @return 时间戳
     */
    public static long string2timestamp(String year, String month, String day) {
        String dateStr = year + "/" + month + "/" + day + " 00:00:00";
        Date date = new Date();
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

}
