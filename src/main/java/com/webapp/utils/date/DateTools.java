/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.time.DateUtils
 *  org.joda.time.DateTime
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.webapp.utils.date;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateTools {
    private static final Logger logger = LoggerFactory.getLogger((Class)DateTools.class);
    private DateTime ofDate;
    private static final ThreadLocal<DateTools> local = new ThreadLocal();
    private static final String[] parsePatterns = new String[]{"EEE MMM dd hh:mm:ss zzz yyyy", "EEE MMM dd HH:mm:ss Z yyyy", "yyyy.MM.dd G 'at' HH:mm:ss z", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy\u5e74MM\u6708dd\u65e5", "HH:mm:ss", "h:mm a"};

    public static String[] getPatterns() {
        return parsePatterns;
    }

    private DateTools(DateTime date) {
        this.ofDate = date;
    }

    private DateTools setData(DateTime ofDate) {
        this.ofDate = ofDate;
        return this;
    }

    private static DateTools localData(DateTime date) {
        if (local.get() == null) {
            local.set(new DateTools(date));
        }
        return local.get().setData(date);
    }

    public static DateTools now() {
        return DateTools.localData(DateTime.now());
    }

    public static DateTools of(String date) {
        return DateTools.localData(DateTools.parse(date));
    }

    public static DateTools of(long date) {
        return DateTools.localData(new DateTime().withMillis(date));
    }

    public static DateTools of(Date date) {
        return DateTools.localData(new DateTime().withMillis(date.getTime()));
    }

    public Date toDate() {
        return this.ofDate.toDate();
    }

    public Calendar toCalendar() {
        return this.ofDate.toCalendar(Locale.getDefault());
    }

    public DateTime toJoda() {
        return this.ofDate;
    }

    public String getDate() {
        return this.ofDate.toString("yyyy-MM-dd");
    }

    public String getTime() {
        return this.ofDate.toString("HH:mm:ss");
    }

    public String getDateTime() {
        return this.ofDate.toString("yyyy-MM-dd HH:mm:ss");
    }

    public long getMillis() {
        return this.ofDate.getMillis();
    }

    public String format() {
        return this.ofDate.toString("yyyy-MM-dd HH:mm:ss");
    }

    public String format(String format) {
        return this.ofDate.toString(format);
    }

    public boolean isAfterNow() {
        return this.ofDate.isAfterNow();
    }

    public boolean isAfter(String date) {
        return this.ofDate.isAfter(DateTools.parse(date).getMillis());
    }

    public boolean isAfter(Date date) {
        return this.ofDate.isAfter(date.getTime());
    }

    public boolean isBeforeNow() {
        return this.ofDate.isBeforeNow();
    }

    public boolean isBefore(String date) {
        return this.ofDate.isBefore(DateTools.parse(date).getMillis());
    }

    public boolean isBefore(Date date) {
        return this.ofDate.isBefore(date.getTime());
    }

    private static DateTime parse(String date) {
        return DateTools.parse(date, parsePatterns);
    }

    private static DateTime parse(String date, String[] parsePattern) {
        DateTime dateTime = new DateTime();
        try {
            dateTime = date.matches("\\d{13}") ? dateTime.withMillis(Long.parseLong(date)) : (DateTools.isLocalEn(date) ? dateTime.withMillis(DateUtils.parseDate((String)date, (Locale)Locale.ENGLISH, (String[])parsePattern).getTime()) : dateTime.withMillis(DateUtils.parseDate((String)date, (String[])parsePattern).getTime()));
            return dateTime;
        }
        catch (ParseException e) {
            logger.error(date + " parse error", (Throwable)e);
            return null;
        }
    }

    private static boolean isLocalEn(String date) {
        return date.matches("\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} [\\+]?\\w{3,4} \\d{4}");
    }

    public static interface FmtDate {
        public static final String Fmt_DateTime_UTC = "EEE MMM dd hh:mm:ss zzz yyyy";
        public static final String Fmt_DateTime_Z = "EEE MMM dd HH:mm:ss Z yyyy";
        public static final String Fmt_DateTime_AT = "yyyy.MM.dd G 'at' HH:mm:ss z";
        public static final String Fmt_DateTime_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        public static final String Fmt_DateTime_TZ_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";
        public static final String Fmt_DateTime_T = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String Fmt_DateTime = "yyyy-MM-dd HH:mm:ss";
        public static final String Fmt_DateTime_NS = "yyyy-MM-dd HH:mm";
        public static final String Fmt_Date = "yyyy-MM-dd";
        public static final String Fmt_DateTime_Slant = "yyyy/MM/dd HH:mm:ss";
        public static final String Fmt_DateTime_SNS = "yyyy/MM/dd HH:mm";
        public static final String Fmt_Date_Slant = "yyyy/MM/dd";
        public static final String Fmt_Date_CN = "yyyy\u5e74MM\u6708dd\u65e5";
        public static final String Fmt_Time = "HH:mm:ss";
        public static final String Fmt_Time_M = "h:mm a";
    }

}
