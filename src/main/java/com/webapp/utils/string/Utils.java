/*     */ package com.webapp.utils.string;
/*     */ 
/*     */ import com.webapp.utils.jpinyin.PinyinFormat;
/*     */ import com.webapp.utils.jpinyin.PinyinHelper;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public final class Utils
/*     */ {
/*     */   public static String toPinyin(String str)
/*     */   {
/*  94 */     return toPinyin(str, "");
/*     */   }
/*     */ 
/*     */   public static String toPinyin(String str, String separator)
/*     */   {
/* 105 */     return PinyinHelper.convertToPinyinString(str, separator, PinyinFormat.WITHOUT_TONE);
/*     */   }
/*     */ 
/*     */   public static String toShortPinyin(String str)
/*     */   {
/* 115 */     return PinyinHelper.getShortPinyin(str);
/*     */   }
/*     */ 
/*     */   public static String toSnake(String str)
/*     */   {
/* 125 */     if (str == null) return null;
/*     */ 
/* 127 */     StringBuilder sb = new StringBuilder();
/* 128 */     boolean prevUpper = false; boolean curUpper = false; boolean nextUpper = false;
/* 129 */     for (int i = 0; i < str.length(); i++) {
/* 130 */       char s = str.charAt(i);
/*     */ 
/* 132 */       prevUpper = curUpper;
/* 133 */       curUpper = i == 0 ? Character.isUpperCase(s) : nextUpper;
/* 134 */       nextUpper = i < str.length() - 1 ? Character.isUpperCase(str.charAt(i + 1)) : true;
/*     */ 
/* 136 */       if (!String.valueOf(s).equals("_")) {
/* 137 */         if ((i > 0) && (curUpper) && ((!nextUpper) || (!prevUpper))) sb.append("_");
/*     */ 
/* 139 */         sb.append(Character.toLowerCase(s));
/*     */       }
/*     */     }
/* 141 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static String toCamel(String str)
/*     */   {
/* 151 */     if (str == null) return null;
/*     */ 
/* 153 */     if (str.contains("_")) str = str.toLowerCase();
/* 154 */     StringBuilder sb = new StringBuilder(str.length());
/* 155 */     boolean upperCase = false;
/* 156 */     for (int i = 0; i < str.length(); i++) {
/* 157 */       char c = str.charAt(i);
/*     */ 
/* 159 */       if (String.valueOf(c).equals("_")) {
/* 160 */         upperCase = true;
/*     */       }
/* 162 */       else if (upperCase) {
/* 163 */         sb.append(Character.toUpperCase(c));
/* 164 */         upperCase = false;
/*     */       } else {
/* 166 */         sb.append(c);
/*     */       }
/*     */     }
/*     */ 
/* 170 */     return StringUtils.uncapitalize(sb.toString());
/*     */   }
/*     */ 
/*     */   public static String toPascal(String str)
/*     */   {
/* 180 */     if (str == null) return null;
/* 181 */     return StringUtils.capitalize(toCamel(str));
/*     */   }
/*     */ 
/*     */   public static String toEmail(String email)
/*     */   {
/* 192 */     return email
/* 192 */       .contains("@gmail") ? 
/* 192 */       "https://accounts.google.com" : StringUtils.isEmpty(email) ? null : 
/* 192 */       new StringBuilder().append("http://mail.").append(email.split("@")[1]).toString();
/*     */   }
/*     */ 
/*     */   public static boolean regexEmail(String email)
/*     */   {
/* 202 */     return (StringUtils.isNotEmpty(email)) && (email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"));
/*     */   }
/*     */ 
/*     */   public static boolean regexMobile(String phone)
/*     */   {
/* 212 */     return (StringUtils.isNotEmpty(phone)) && (phone.matches("^(13[0-9]|14[5,7]|15[^4,\\D]|17[6-8]|18[0-9])\\d{8}$"));
/*     */   }
/*     */ 
/*     */   public static boolean regexIdcard(String idcard)
/*     */   {
/* 222 */     return (StringUtils.isNotEmpty(idcard)) && (idcard.matches("^[1-9]([0-9]{14}|[0-9]{17})$"));
/*     */   }
/*     */ 
/*     */   public static String safedEmail(String email)
/*     */   {
/* 232 */     return safedEmail(email, 3);
/*     */   }
/*     */ 
/*     */   public static String safedEmail(String email, int len)
/*     */   {
/* 243 */     if (StringUtils.isNotEmpty(email))
/* 244 */       return email.replaceAll(new StringBuilder().append("(.{").append(len).append("})(?=@)").toString(), "***");
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   public static String safedMobile(String mobile)
/*     */   {
/* 255 */     if (StringUtils.isNotEmpty(mobile))
/* 256 */       return mobile.replaceAll("(?<=\\d{3})(.{4})(?=\\d{4})", "****");
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   public static <T> String split(List<T> list)
/*     */   {
/* 267 */     return split(list, ",");
/*     */   }
/*     */ 
/*     */   public static <T> String split(List<T> list, String split)
/*     */   {
/* 278 */     StringBuffer result = new StringBuffer();
/*     */ 
/* 280 */     for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object item = localIterator.next();
/* 281 */       result.append(new StringBuilder().append(item).append(split).toString());
/*     */     }
/* 283 */     return StringUtils.removeEnd(result.toString(), split);
/*     */   }
/*     */ 
/*     */   public static String delTail(String str)
/*     */   {
/* 293 */     return delTail(str, ",");
/*     */   }
/*     */ 
/*     */   public static String delTail(String str, String remove)
/*     */   {
/* 304 */     if (StringUtils.isNotEmpty(str))
/* 305 */       return StringUtils.removeEnd(str, remove);
/* 306 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isChinese(String str)
/*     */   {
/* 315 */     return isChinese(str, false);
/*     */   }
/*     */ 
/*     */   public static boolean isChinese(String str, boolean hasSymbols)
/*     */   {
/* 324 */     char[] ch = str.toCharArray();
/* 325 */     for (int i = 0; i < ch.length; i++) {
/* 326 */       char c = ch[i];
/* 327 */       if (isChinese(c, hasSymbols)) {
/* 328 */         return true;
/*     */       }
/*     */     }
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isChinese(char c, boolean hasSymbols) {
/* 335 */     Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
/* 336 */     if ((ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) || (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A))
/*     */     {
/* 339 */       return true;
/*     */     }
/* 341 */     if ((hasSymbols) && (
/* 342 */       (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) || (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) || (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)))
/*     */     {
/* 345 */       return true;
/*     */     }
/*     */ 
/* 348 */     return false;
/*     */   }
/*     */ 
/*     */   public static String ascii2native(String ascii)
/*     */   {
/* 353 */     List ascii_s = new ArrayList();
/* 354 */     String regex = "\\\\u[0-9,a-f,A-F]{4}";
/* 355 */     Pattern p = Pattern.compile(regex);
/* 356 */     Matcher m = p.matcher(ascii);
/* 357 */     while (m.find()) {
/* 358 */       ascii_s.add(m.group());
/*     */     }
/* 360 */     int i = 0; for (int j = 2; i < ascii_s.size(); i++) {
/* 361 */       String code = ((String)ascii_s.get(i)).substring(j, j + 4);
/* 362 */       char ch = (char)Integer.parseInt(code, 16);
/* 363 */       ascii = ascii.replace((CharSequence)ascii_s.get(i), String.valueOf(ch));
/*     */     }
/* 365 */     return ascii;
/*     */   }
/*     */ 
/*     */   public static String str2Hex(String str) throws UnsupportedEncodingException {
/* 369 */     String hexRaw = String.format("%x", new Object[] { new BigInteger(1, str.getBytes("UTF-8")) });
/* 370 */     char[] hexRawArr = hexRaw.toCharArray();
/* 371 */     StringBuilder hexFmtStr = new StringBuilder();
/* 372 */     String SEP = "\\x";
/* 373 */     for (int i = 0; i < hexRawArr.length; i++) {
/* 374 */       hexFmtStr.append("\\x").append(hexRawArr[i]).append(hexRawArr[(++i)]);
/*     */     }
/* 376 */     return hexFmtStr.toString();
/*     */   }
/*     */ 
/*     */   public static String hex2Str(String str) throws UnsupportedEncodingException {
/* 380 */     String[] strArr = str.split("\\\\");
/* 381 */     byte[] byteArr = new byte[strArr.length - 1];
/* 382 */     for (int i = 1; i < strArr.length; i++) {
/* 383 */       Integer hexInt = Integer.decode(new StringBuilder().append("0").append(strArr[i]).toString());
/* 384 */       byteArr[(i - 1)] = hexInt.byteValue();
/*     */     }
/*     */ 
/* 387 */     return new String(byteArr, "UTF-8");
/*     */   }
/*     */ 
/*     */   public static abstract interface Symbol
/*     */   {
/*     */     public static final String Enter = "\n";
/*     */     public static final String Tab = "\t";
/*     */     public static final String Empty = "";
/*     */     public static final String Space = " ";
/*     */     public static final String Dot = ".";
/*     */     public static final String Comma = ",";
/*     */     public static final String Colon = ":";
/*     */     public static final String Semicolon = ";";
/*     */     public static final String LineThrough = "-";
/*     */     public static final String LineUnder = "_";
/*     */     public static final String Pound = "#";
/*     */     public static final String Question = "?";
/*     */     public static final String And = "&";
/*     */     public static final String Dollar = "$";
/*     */     public static final String Percent = "%";
/*     */     public static final String At = "@";
/*     */     public static final String Slash = "/";
/*     */     public static final String Backslash = "\\";
/*     */     public static final String LParen = "(";
/*     */     public static final String RParen = ")";
/*     */     public static final String LBrace = "{";
/*     */     public static final String RBrace = "}";
/*     */     public static final String LBracket = "[";
/*     */     public static final String RBracket = "]";
/*     */   }
/*     */ 
/*     */   public static abstract interface Charsets
/*     */   {
/*  30 */     public static final String uft8 = StandardCharsets.UTF_8.name();
/*     */     public static final String gbk = "gbk";
/*  32 */     public static final String iso = StandardCharsets.ISO_8859_1.name();
/*     */   }
/*     */ }

 