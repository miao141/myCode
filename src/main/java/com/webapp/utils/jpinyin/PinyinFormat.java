/*    */ package com.webapp.utils.jpinyin;
/*    */ 
/*    */ public class PinyinFormat
/*    */ {
/*    */   private String name;
/* 11 */   public static final PinyinFormat WITH_TONE_MARK = new PinyinFormat("WITH_TONE_MARK");
/* 12 */   public static final PinyinFormat WITHOUT_TONE = new PinyinFormat("WITHOUT_TONE");
/* 13 */   public static final PinyinFormat WITH_TONE_NUMBER = new PinyinFormat("WITH_TONE_NUMBER");
/*    */ 
/*    */   protected PinyinFormat(String name) {
/* 16 */     this.name = name;
/*    */   }
/*    */ 
/*    */   protected String getName() {
/* 20 */     return this.name;
/*    */   }
/*    */ }

