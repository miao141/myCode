/*    */ package com.webapp.utils.jpinyin;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import java.util.zip.ZipInputStream;
/*    */ 
/*    */ public class PinyinResource
/*    */ {
/* 16 */   private static final Logger LOGGER = Logger.getLogger(PinyinResource.class.getName());
/*    */   private static final String base = "/data_db";
/*    */ 
/*    */   private static Properties getResource(String resourceName)
/*    */   {
/* 20 */     ZipInputStream zip = new ZipInputStream(PinyinResource.class.getResourceAsStream(resourceName));
/*    */     try
/*    */     {
/* 23 */       zip.getNextEntry();
/* 24 */       Properties p = new Properties();
/* 25 */       p.load(zip);
/* 26 */       zip.close();
/* 27 */       return p;
/*    */     } catch (IOException e) {
/* 29 */       LOGGER.log(Level.WARNING, "Exception in loading PinyinResource", e);
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   protected static Properties getPinyinTable() {
/* 35 */     String resourceName = "/data_db/pinyin.db";
/* 36 */     return getResource(resourceName);
/*    */   }
/*    */ 
/*    */   protected static Properties getMutilPintinTable() {
/* 40 */     String resourceName = "/data_db/mutil_pinyin.db";
/* 41 */     return getResource(resourceName);
/*    */   }
/*    */ 
/*    */   protected static Properties getChineseTable() {
/* 45 */     String resourceName = "/data_db/chinese.db";
/* 46 */     return getResource(resourceName);
/*    */   }
/*    */ }

