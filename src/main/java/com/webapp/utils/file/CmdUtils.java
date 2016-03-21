/*    */ package com.webapp.utils.file;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ public final class CmdUtils
/*    */ {
/*    */   public static String exec(String cmd, String encoding)
/*    */   {
/* 11 */     String result = "";
/*    */     try {
/* 13 */       Process exec = Runtime.getRuntime().exec(cmd);
/* 14 */       result = IOUtils.toString(exec.getInputStream(), encoding);
/*    */     } catch (IOException e) {
/* 16 */       e.printStackTrace();
/*    */     }
/* 18 */     return result;
/*    */   }
/*    */   public static String javap(String file, String encoding) {
/* 21 */     return exec("javap -c " + file, encoding);
/*    */   }
/*    */ }

 