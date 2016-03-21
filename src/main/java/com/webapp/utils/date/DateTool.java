/*     */ package com.webapp.utils.date;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.ZoneId;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.lang3.time.DateUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public final class DateTool
/*     */ {
/*  51 */   private static final Logger logger = LoggerFactory.getLogger(DateTool.class);
/*     */   private LocalDateTime dt;
/*  54 */   private static final ThreadLocal<DateTool> local = new ThreadLocal();
/*  55 */   private static final String[] parsePatterns = { "EEE MMM dd hh:mm:ss zzz yyyy", "EEE MMM dd HH:mm:ss Z yyyy", "yyyy.MM.dd G 'at' HH:mm:ss z", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy年MM月dd日", "HH:mm:ss", "h:mm a" };
/*     */ 
/*     */   private DateTool(LocalDateTime dt)
/*     */   {
/*  30 */     this.dt = dt;
/*     */   }
/*     */ 
/*     */   public static LocalDate nowDate() {
/*  34 */     return LocalDate.now();
/*     */   }
/*     */   public static LocalTime nowTime() {
/*  37 */     return LocalTime.now();
/*     */   }
/*     */   public static LocalDateTime now() {
/*  40 */     return LocalDateTime.now();
/*     */   }
/*     */ 
/*     */   public static String[] getPatterns()
/*     */   {
/*  74 */     return parsePatterns;
/*     */   }
/*     */ 
/*     */   public static boolean isLocalEn(String date) {
/*  78 */     return date.matches("\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} [\\+]?\\w{3,4} \\d{4}");
/*     */   }
/*     */ 
/*     */   public static DateTool of(String date)
/*     */   {
/* 117 */     return localData(parse(date));
/*     */   }
/*     */ 
/*     */   public static DateTool of(long date) {
/* 121 */     return localData(parse(Long.valueOf(date)));
/*     */   }
/*     */ 
/*     */   public static DateTool of(Date date) {
/* 125 */     return localData(parse(Long.valueOf(date.getTime())));
/*     */   }
/*     */ 
/*     */   private static DateTool localData(LocalDateTime dt) {
/* 129 */     if (local.get() == null) local.set(new DateTool(dt));
/* 130 */     return ((DateTool)local.get()).setDate(dt);
/*     */   }
/*     */ 
/*     */   private DateTool setDate(LocalDateTime dt) {
/* 134 */     this.dt = dt;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */   private static LocalDateTime parse(String date) {
/* 139 */     return parse(date, parsePatterns);
/*     */   }
/*     */ 
/*     */   private static LocalDateTime parse(String date, String[] parsePattern) {
/* 143 */     Long result = null;
/*     */     try {
/* 145 */       if (date.matches("\\d{13}"))
/* 146 */         result = Long.valueOf(Long.parseLong(date));
/* 147 */       else if (isLocalEn(date))
/* 148 */         result = Long.valueOf(DateUtils.parseDate(date, Locale.ENGLISH, parsePattern).getTime());
/*     */       else {
/* 150 */         result = Long.valueOf(DateUtils.parseDate(date, parsePattern).getTime());
/*     */       }
/* 152 */       return parse(result);
/*     */     } catch (ParseException e) {
/* 154 */       logger.error(date + " parse error", e);
/*     */     }
/* 156 */     return LocalDateTime.now();
/*     */   }
/*     */   private static LocalDateTime parse(Long date) {
/* 159 */     return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.longValue()), ZoneId.systemDefault());
/*     */   }
/*     */ 
/*     */   public String format()
/*     */   {
/* 164 */     return this.dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ"));
/*     */   }
/*     */ 
/*     */   public static abstract interface FmtDate
/*     */   {
/*     */     public static final String Fmt_DateTime_UTC = "EEE MMM dd hh:mm:ss zzz yyyy";
/*     */     public static final String Fmt_DateTime_Z = "EEE MMM dd HH:mm:ss Z yyyy";
/*     */     public static final String Fmt_DateTime_AT = "yyyy.MM.dd G 'at' HH:mm:ss z";
/*     */     public static final String Fmt_DateTime_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */     public static final String Fmt_DateTime_TZ_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";
/*     */     public static final String Fmt_DateTime_T = "yyyy-MM-dd'T'HH:mm:ss";
/*     */     public static final String Fmt_DateTime = "yyyy-MM-dd HH:mm:ss";
/*     */     public static final String Fmt_DateTime_NS = "yyyy-MM-dd HH:mm";
/*     */     public static final String Fmt_Date = "yyyy-MM-dd";
/*     */     public static final String Fmt_DateTime_Slant = "yyyy/MM/dd HH:mm:ss";
/*     */     public static final String Fmt_DateTime_SNS = "yyyy/MM/dd HH:mm";
/*     */     public static final String Fmt_Date_Slant = "yyyy/MM/dd";
/*     */     public static final String Fmt_Date_CN = "yyyy年MM月dd日";
/*     */     public static final String Fmt_Time = "HH:mm:ss";
/*     */     public static final String Fmt_Time_M = "h:mm a";
/*     */   }
/*     */ }

 