/*    */ package com.webapp.utils.collections;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class DataShowUtils
/*    */ {
/*    */   public static <V> void viewList(List<V> list)
/*    */   {
/* 15 */     for (int i = 0; i < list.size(); i++)
/* 16 */       System.out.println(list.get(i));
/*    */   }
/*    */ 
/*    */   public static <V> void viewSet(Set<V> set)
/*    */   {
/* 21 */     Iterator iterator = set.iterator();
/* 22 */     while (iterator.hasNext())
/* 23 */       System.out.println(iterator.next());
/*    */   }
/*    */ 
/*    */   public static <K, V> void viewMapKey(Map<K, V> map)
/*    */   {
/* 28 */     Iterator iterator = map.keySet().iterator();
/* 29 */     while (iterator.hasNext())
/* 30 */       System.out.println(iterator.next());
/*    */   }
/*    */ 
/*    */   public static <K, V> void viewMapVal(Map<K, V> map)
/*    */   {
/* 35 */     Collection temp = map.values();
/* 36 */     for (Iterator i = temp.iterator(); i.hasNext(); )
/* 37 */       System.out.println(i.next());
/*    */   }
/*    */ 
/*    */   public static <K, V> void viewMapKeyVal(Map<K, V> map)
/*    */   {
/* 42 */     Iterator entry = map.entrySet().iterator();
/* 43 */     while (entry.hasNext()) {
/* 44 */       Map.Entry temp = (Map.Entry)entry.next();
/* 45 */       System.out.println(temp.getKey() + "-" + temp.getValue());
/*    */     }
/*    */   }
/*    */ }

