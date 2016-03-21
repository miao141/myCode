/*     */ package com.webapp.utils.enums;
/*     */ 
/*     */ import com.webapp.utils.clz.ClzUtils;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class EnumUtils
/*     */ {
/*     */   
/*     */   public static <E extends Enum<E>> List<String> getList(Class<E> clz, String prop, E[] excludes)
/*     */   {
/*  26 */     Field field = ClzUtils.getField(clz, prop);
/*     */ 
/*  28 */     List result = new ArrayList();
/*  29 */     if (field == null) return result;
/*     */ 
/*  31 */     EnumSet allOf = EnumSet.allOf(clz);
/*  32 */     if (excludes != null) allOf.removeAll(Arrays.asList(excludes));
/*     */ 
/*  34 */     Iterator iterator = allOf.iterator();
/*  35 */     while (iterator.hasNext()) {
/*  36 */       Enum next = (Enum)iterator.next();
/*  37 */       result.add(ClzUtils.getFieldVal(field, next).toString());
/*     */     }
/*  39 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>, T> List<T> getList(Class<E> clz, String prop, Class<T> returnType, E[] excludes)
/*     */   {
/*  45 */     Field field = ClzUtils.getField(clz, prop);
/*     */ 
/*  47 */     List result = new ArrayList();
/*  48 */     if (field == null) return result;
/*     */ 
/*  50 */     EnumSet allOf = EnumSet.allOf(clz);
/*  51 */     if (excludes != null) allOf.removeAll(Arrays.asList(excludes));
/*     */ 
/*  53 */     Iterator iterator = allOf.iterator();
/*  54 */     while (iterator.hasNext()) {
/*  55 */       Enum next = (Enum)iterator.next();
/*  56 */       result.add(ClzUtils.getFieldVal(field, next));
/*     */     }
/*  58 */     return result;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> String valueOf(E enumEle, String prop)
/*     */   {
/*  68 */     Field field = ClzUtils.getField(enumEle.getClass(), prop);
/*  69 */     if (field == null) return null;
/*     */ 
/*  71 */     return ClzUtils.getFieldVal(field, enumEle).toString();
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> boolean isNotExist(Class<E> clz, String prop, Object value)
/*     */   {
/*  81 */     return !isExist(clz, prop, value);
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> boolean isExist(Class<E> clz, String prop, Object value)
/*     */   {
/*  91 */     return getEnum(clz, prop, value) != null;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> E getEnum(Class<E> clz, String name)
/*     */   {
/* 102 */     Iterator iterator = EnumSet.allOf(clz).iterator();
/* 103 */     while (iterator.hasNext()) {
/* 104 */       Enum next = (Enum)iterator.next();
/* 105 */       if (next.name().equals(name)) {
/* 106 */         return (E) next;
/*     */       }
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> E getEnum(Class<E> clz, String prop, Object value)
/*     */   {
/* 120 */     Field field = ClzUtils.getField(clz, prop);
/* 121 */     if (field == null) return null;
/*     */ 
/* 123 */     Iterator iterator = EnumSet.allOf(clz).iterator();
/* 124 */     while (iterator.hasNext()) {
/* 125 */       Enum next = (Enum)iterator.next();
/* 126 */       if (ClzUtils.getFieldVal(field, next).equals(value)) {
/* 127 */         return (E) next;
/*     */       }
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> List<E> getEnums(E[] all, E[] excludes) {
/* 135 */     List result = new ArrayList(Arrays.asList(all));
/* 136 */     result.removeAll(Arrays.asList(excludes));
/*     */ 
/* 138 */     return result;
/*     */   }
/*     */ }

