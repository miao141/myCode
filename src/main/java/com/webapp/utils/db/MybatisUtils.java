/*    */ package com.webapp.utils.db;
/*    */ 
/*    */ import com.webapp.utils.string.Utils;
/*    */ import java.io.PrintStream;
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ public final class MybatisUtils
/*    */ {
/*    */   public static <T> void propSet(Class<T> clz)
/*    */   {
/* 13 */     StringBuffer columns = new StringBuffer();
/* 14 */     String name = clz.getSimpleName();
/* 15 */     String camel = Utils.toCamel(name);
/* 16 */     columns.append(new StringBuilder().append(name).append(" ").append(camel).append(" = new ").append(name).append("();\n").toString());
/* 17 */     Field[] fields = clz.getDeclaredFields();
/* 18 */     for (Field field : fields) {
/* 19 */       String col = field.getName();
/* 20 */       columns.append(new StringBuilder().append(camel).append(".set").append(Utils.toPascal(col)).append("();\n").toString());
/*    */     }
/* 22 */     String result = Utils.delTail(columns.toString());
/* 23 */     System.out.println(result);
/*    */   }
/*    */ 
/*    */   public static <T> void insertCols(Class<T> clz) {
/* 27 */     sql_insert(clz, false);
/*    */   }
/*    */ 
/*    */   public static <T> void insertVals(Class<T> clz) {
/* 31 */     sql_insert(clz, true);
/*    */   }
/*    */ 
/*    */   private static <T> void sql_insert(Class<T> clz, boolean isProp) {
/* 35 */     StringBuffer columns = new StringBuffer();
/* 36 */     Field[] fields = clz.getDeclaredFields();
/* 37 */     for (Field field : fields) {
/* 38 */       String col = field.getName();
/* 39 */       if (isProp)
/* 40 */         columns.append(new StringBuilder().append("#{").append(col).append("},").toString());
/*    */       else {
/* 42 */         columns.append(new StringBuilder().append(Utils.toSnake(col)).append(',').toString());
/*    */       }
/*    */     }
/* 45 */     String result = Utils.delTail(columns.toString());
/* 46 */     System.out.println(result);
/*    */   }
/*    */ 
/*    */   public static <T> void searchCols(Class<T> clz) {
/* 50 */     StringBuffer columns = new StringBuffer();
/* 51 */     Field[] fields = clz.getDeclaredFields();
/* 52 */     for (Field field : fields) {
/* 53 */       String col = field.getName();
/* 54 */       String consts = Utils.toSnake(col);
/* 55 */       columns.append(new StringBuilder().append(consts).append(consts.contains("_") ? new StringBuilder().append(" ").append(col).toString() : "").append(",").toString());
/*    */     }
/* 57 */     String result = Utils.delTail(columns.toString());
/* 58 */     System.out.println(result);
/*    */   }
/*    */ 
/*    */   public static <T> void updateCols(Class<T> clz) {
/* 62 */     StringBuffer columns = new StringBuffer();
/* 63 */     Field[] fields = clz.getDeclaredFields();
/* 64 */     for (Field field : fields) {
/* 65 */       String col = field.getName();
/* 66 */       String consts = Utils.toSnake(col);
/* 67 */       columns.append(new StringBuilder().append(consts).append("=#{").append(col).append("},").toString());
/*    */     }
/* 69 */     String result = Utils.delTail(columns.toString());
/* 70 */     System.out.println(result);
/*    */   }
/*    */ 
/*    */   public static <T> void propConst(Class<T> clz) {
/* 74 */     StringBuffer columns = new StringBuffer();
/* 75 */     Field[] fields = clz.getDeclaredFields();
/* 76 */     for (Field field : fields) {
/* 77 */       String col = field.getName();
/* 78 */       columns.append("public final static String ");
/*    */ 
/* 80 */       String consts = Utils.toSnake(col);
/* 81 */       columns.append(new StringBuilder().append(consts.toUpperCase()).append(" = \"").append(consts.toLowerCase()).append("\";\n").toString());
/*    */     }
/* 83 */     String result = Utils.delTail(columns.toString());
/* 84 */     System.out.println(result);
/*    */   }
/*    */ }

