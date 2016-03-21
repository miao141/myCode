/*     */ package com.webapp.utils.jpinyin;
/*     */ 
/*     */ import java.util.Iterator;
import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class ChineseHelper
/*     */ {
/*  14 */   private static final Properties CHINESETABLE = PinyinResource.getChineseTable();
/*     */ 
/*     */   public static char convertToSimplifiedChinese(char c)
/*     */   {
/*  24 */     if (isTraditionalChinese(c)) {
/*  25 */       return CHINESETABLE.getProperty(String.valueOf(c)).charAt(0);
/*     */     }
/*  27 */     return c;
/*     */   }
/*     */ 
/*     */   public static char convertToTraditionalChinese(char c)
/*     */   {
/*  38 */     String hanzi = String.valueOf(c);
/*  39 */     if (CHINESETABLE.containsValue(hanzi)) {
/*  40 */       Iterator itr = CHINESETABLE.entrySet().iterator();
/*  41 */       while (itr.hasNext()) {
/*  42 */         Map.Entry e = (Map.Entry)itr.next();
/*  43 */         if (e.getValue().toString().equals(hanzi)) {
/*  44 */           return e.getKey().toString().charAt(0);
/*     */         }
/*     */       }
/*     */     }
/*  48 */     return c;
/*     */   }
/*     */ 
/*     */   public static String convertToSimplifiedChinese(String str)
/*     */   {
/*  59 */     StringBuilder sb = new StringBuilder();
/*  60 */     int i = 0; for (int len = str.length(); i < len; i++) {
/*  61 */       char c = str.charAt(i);
/*  62 */       sb.append(convertToSimplifiedChinese(c));
/*     */     }
/*  64 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static String convertToTraditionalChinese(String str)
/*     */   {
/*  75 */     StringBuilder sb = new StringBuilder();
/*  76 */     int i = 0; for (int len = str.length(); i < len; i++) {
/*  77 */       char c = str.charAt(i);
/*  78 */       sb.append(convertToTraditionalChinese(c));
/*     */     }
/*  80 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static boolean isTraditionalChinese(char c)
/*     */   {
/*  91 */     return CHINESETABLE.containsKey(String.valueOf(c));
/*     */   }
/*     */ 
/*     */   public static boolean isChinese(char c)
/*     */   {
/* 102 */     String regex = "[\\u4e00-\\u9fa5]";
/* 103 */     return String.valueOf(c).matches(regex);
/*     */   }
/*     */ }

 