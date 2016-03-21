/*    */ package com.webapp.utils.clz;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public final class ClzUtils
/*    */ {
/*    */   public static boolean hasAnno(Class<?> clz, Class<? extends Annotation> anno)
/*    */   {
/* 13 */     return clz.isAnnotationPresent(anno);
/*    */   }
/*    */ 
/*    */   public static boolean hasAnno(Method method, Class<? extends Annotation> anno) {
/* 17 */     return method.isAnnotationPresent(anno);
/*    */   }
/*    */ 
/*    */   public static <T extends Annotation> T getAnno(Method method, Class<T> anno) {
/* 21 */     return method.getAnnotation(anno);
/*    */   }
/*    */ 
/*    */   public static <T extends Annotation> T getAnno(Class<T> clz, Class<T> anno) {
/* 25 */     return clz.getAnnotation(anno);
/*    */   }
/*    */ 
/*    */   public static <T extends Annotation> T getAnnoIfClz(Class<T> clz, Method method, Class<T> anno) {
/* 29 */     Annotation tAnno = null;
/* 30 */     if (clz.isAnnotationPresent(anno))
/* 31 */       tAnno = clz.getAnnotation(anno);
/* 32 */     else if (method.isAnnotationPresent(anno)) {
/* 33 */       tAnno = method.getAnnotation(anno);
/*    */     }
/* 35 */     return (T) tAnno;
/*    */   }
/*    */ 
/*    */   public static <T extends Annotation> T getAnnoIfMethod(Class<T> clz, Method method, Class<T> anno) {
/* 39 */     Annotation tAnno = null;
/* 40 */     if (method.isAnnotationPresent(anno))
/* 41 */       tAnno = method.getAnnotation(anno);
/* 42 */     else if (clz.isAnnotationPresent(anno)) {
/* 43 */       tAnno = clz.getAnnotation(anno);
/*    */     }
/* 45 */     return (T) tAnno;
/*    */   }
/*    */ 
/*    */   public static <T> Method getMethod(Class<T> clz, String prop, Class<?>[] paramTypes) {
/* 49 */     Method method = null;
/*    */     try {
/* 51 */       method = clz.getMethod(prop, paramTypes);
/*    */     } catch (NoSuchMethodException|SecurityException e) {
/* 53 */       e.printStackTrace();
/*    */     }
/* 55 */     return method;
/*    */   }
/*    */ 
/*    */   public static Method getGetMethod(Class<?> clz, String prop) {
/* 59 */     String get = "get" + StringUtils.capitalize(prop);
/* 60 */     return getMethod(clz, get, new Class[0]);
/*    */   }
/*    */ 
/*    */   public static <T> T invoke(Method method, Object instance, Class<T> returnClz)
/*    */   {
/* 66 */     Object result = null;
/*    */     try {
/* 68 */       result = method.invoke(instance, new Object[0]);
/*    */     } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 70 */       e.printStackTrace();
/*    */     }
/* 72 */     return (T) result;
/*    */   }
/*    */ 
/*    */   public static boolean hasField(Class<?> clz, String prop) {
/* 76 */     return getField(clz, prop) != null;
/*    */   }
/*    */ 
/*    */   public static Field getField(Class<?> clz, String prop) {
/* 80 */     Field field = null;
/*    */     try {
/* 82 */       field = clz.getDeclaredField(prop);
/*    */     } catch (NoSuchFieldException|SecurityException e) {
/* 84 */       e.printStackTrace();
/*    */     }
/* 86 */     return field;
/*    */   }
/*    */ 
/*    */   public static Object getFieldVal(Field field, Object instance) {
/* 90 */     Object result = null;
/*    */     try {
/* 92 */       if (!field.isAccessible()) field.setAccessible(true);
/* 93 */       result = field.get(instance);
/*    */     } catch (IllegalAccessException|SecurityException e) {
/* 95 */       e.printStackTrace();
/*    */     }
/* 97 */     return result;
/*    */   }
/*    */ }

 