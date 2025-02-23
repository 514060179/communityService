package com.newland.property.utils.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    private static final String FORMAT_0 = "yyyy-MM-dd HH:mm:ss";

    private static final String FORMAT_1 = "yyyy-MM-dd";

    private static final String FORMAT_2 = "HH:mm:ss";

    public DateUtils() {
    }

    @SuppressWarnings("unused")
    public static String getDateStr(Date date, String s) {
        SimpleDateFormat simpledateformat;
        return (simpledateformat = new SimpleDateFormat(s)).format(date);
    }

    @SuppressWarnings("unused")
    public static String getDateStr(long l, String s) {
        SimpleDateFormat simpledateformat;
        return (simpledateformat = new SimpleDateFormat(s)).format(new Date(l));
    }

    public static String getNow(String s) {
        return getDateStr(System.currentTimeMillis(), s);
    }

    public static Date getDate(String s, String s1, long l) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
        Date date;
        try {
            date = simpledateformat.parse(s);
        } catch (Exception _ex) {
            date = new Date(l);
        }
        return date;
    }

    public static Date getDate(String s, String s1) {
        return getDate(s, s1, 0L);
    }

    public static long getTime(String s, String s1, long l) {
        return getDate(s, s1, l).getTime();
    }

    public static long getTime(String s, String s1) {
        return getTime(s, s1, 0L);
    }

    public static String convert(String s, String s1, String s2) {
        Date date = getDate(s, s1);
        if (null == date)
            return "";
        else
            return getDateStr(date, s2);
    }

    public static String convert(String s, String s1, String s2, char c) {
        char[] ac = s.toCharArray();
        for (int i = 0; i < ac.length; i++)
            if (c == s.charAt(i))
                ac[i] = '-';

        Date date = getDate(new String(ac), s1.replace(c, '-'));
        char[] ac1 = null != date ? getDateStr(date, s2.replace(c, '-')).toCharArray() : new char[0];
        for (int j = 0; j < ac1.length; j++)
            if (c == s.charAt(j))
                ac1[j] = s.charAt(j);

        return new String(ac1);
    }

    public static String getSeason(String s, String s1) {
        String s2 = "";
        Date date = getDate(s, s1);
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        String s3 = "yyyy";
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s3);
        s2 = s2 + simpledateformat.format(date);
        gregoriancalendar.setTime(date);
        return s2 = s2 + getSeason(gregoriancalendar.get(2));
    }

    private static String getSeason(int i) {
        switch (i) {
            case 0:
                return "01";

            case 1:
                return "01";

            case 2:
                return "01";

            case 3:
                return "02";

            case 4:
                return "02";

            case 5:
                return "02";

            case 6:
                return "03";

            case 7:
                return "03";

            case 8:
                return "03";

            case 9:
                return "04";

            case 10:
                return "04";

            case 11:
                return "04";
        }
        return "";
    }

    @SuppressWarnings("unused")
    public static int getDayInterval(long l, long l1) {
        int i;
        return i = (int) ((l - l1) / 0x5265c00L);
    }

    public static long addDays(long l, int i) {
        return l + (long) i * 0x5265c00L;
    }

    public static final String NowStr() {
        return DatetimetoStr(new Date());
    }

    public static final String DatetimetoStr(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(FORMAT_0);
        return simpledateformat.format(date);
    }

    @SuppressWarnings("unused")
    public static long startOfDay(long l) {
        String s;
        return getDate(s = getDateStr(l, "yyyyMMdd"), "yyyyMMdd").getTime();
    }

    public static long endOfDay(long l) {
        return (startOfDay(l) + 0x5265c00L) - 1L;
    }

    public static String getDayOfWeekStr(long l) {
        GregorianCalendar gregoriancalendar;
        (gregoriancalendar = new GregorianCalendar()).setTimeInMillis(l);
        int i = gregoriancalendar.get(7);
        String s = "";
        switch (i) {
            case 1:
                s = "\u5468\u65E5";
                break;

            case 2:
                s = "\u5468\u4E00";
                break;

            case 3:
                s = "\u5468\u4E8C";
                break;

            case 4:
                s = "\u5468\u4E09";
                break;

            case 5:
                s = "\u5468\u56DB";
                break;

            case 6:
                s = "\u5468\u4E94";
                break;

            case 7:
                s = "\u5468\u516D";
                break;
        }
        return s;
    }

    @SuppressWarnings("deprecation")
    public static boolean checkCurTimeIsAPeriod(String startTime, String endTime) {
        Date nowTime = new Date();
        String time = nowTime.getHours() + ":" + nowTime.getMinutes();
        return time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0;
    }

    public static String get14StrCurrentTime() {
        Date date = new Date();
        String s = "";
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
        s = s + simpledateformat.format(date);
        simpledateformat = new SimpleDateFormat("HHmmss");
        return s = s + simpledateformat.format(date);
    }

    public static String get14StrTime(Calendar calendar) {
        Date date = calendar.getTime();
        String s = "";
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
        s = s + simpledateformat.format(date);
        simpledateformat = new SimpleDateFormat("HHmmss");
        return s = s + simpledateformat.format(date);
    }

    public static String getTimeStrBySplitor(String s, String s1) {
        if (s == null || s.length() < 8)
            return s;
        if (s.length() < 14)
            return s.substring(0, 4) + s1 + s.substring(4, 6) + s1 + s.substring(6, 8);
        else
            return s.substring(0, 4) + s1 + s.substring(4, 6) + s1 + s.substring(6, 8) + " " + s.substring(8, 10) + ":" + s.substring(10, 12) + ":" + s.substring(12, 14);
    }

    public static String get14Str(String s) {
        return s = StringUtil.replace(s = StringUtil.replace(s = StringUtil.replace(s, "-", ""), ":", ""), " ", "");
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(cal.getTime());
        return today;
    }

    @SuppressWarnings("deprecation")
    public static String getDateFormString(String s) {
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = dateFm.format(new Date(s));
        return dateTime;
    }

    public static Date reverse2Date(String date) {

        SimpleDateFormat simple = null;

        switch (date.trim().length()) {

            case 19:
                simple = new SimpleDateFormat(FORMAT_0);

                break;

            case 10:

                simple = new SimpleDateFormat(FORMAT_1);

                break;

            case 8:
                simple = new SimpleDateFormat(FORMAT_2);

                break;

            default:

                break;

        }

        try {

            return simple.parse(date.trim());

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return null;

    }

    public static java.sql.Date reverse2SqlDate(String date) {

        SimpleDateFormat simple = null;

        switch (date.trim().length()) {

            case 19:
                simple = new SimpleDateFormat(FORMAT_0);

                break;

            case 10:

                simple = new SimpleDateFormat(FORMAT_1);

                break;

            case 8:
                simple = new SimpleDateFormat(FORMAT_2);

                break;

            default:

                break;

        }

        try {

            java.sql.Date sqldate = new java.sql.Date(simple.parse(date.trim()).getTime());
            return sqldate;

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return null;

    }

    public static java.sql.Date getBeforeAfterDate(String datestr, int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date olddate = null;
        try {
            df.setLenient(false);
            olddate = new java.sql.Date(df.parse(datestr).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static long getDaysBetween(String beginDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date d1 = sdf.parse(beginDate);
            Date d2 = sdf.parse(endDate);
            long daysBetween = (d2.getTime() - d1.getTime() + 1000000) / (3600 * 24 * 1000);
            return daysBetween;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static void main(String[] args) {
        System.out.print(DateUtils.getCurrentDate());

    }
}
