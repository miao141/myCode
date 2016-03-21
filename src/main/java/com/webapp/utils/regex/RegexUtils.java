/*    */ package com.webapp.utils.regex;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public final class RegexUtils
/*    */ {
/*    */   public static String match(String data, String regex)
/*    */   {
/* 10 */     Matcher matcher = Pattern.compile(regex).matcher(data);
/* 11 */     return matcher.find() ? matcher.group() : "";
/*    */   }
/*    */ 
/*    */   public static boolean isMatch(String data, String regex) {
/* 15 */     Matcher matcher = Pattern.compile(regex).matcher(data);
/* 16 */     return matcher.matches();
/*    */   }
/*    */ }

 