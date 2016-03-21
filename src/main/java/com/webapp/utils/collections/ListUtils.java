/*    */ package com.webapp.utils.collections;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class ListUtils
/*    */ {
/*    */   public static <T> List<T> unique(List<T> list)
/*    */   {
/* 10 */     Set set = new HashSet(list);
/* 11 */     list.clear();
/* 12 */     list.addAll(set);
/* 13 */     return list;
/*    */   }
/*    */ }

