/*    */ package com.webapp.utils.json;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.parser.Feature;
/*    */ import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
/*    */ import com.webapp.utils.string.Utils;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class JSONBeanUtils<T>
/*    */ {
/*    */   private String json;
/*    */   private Class<T> clz;
/*    */ 
/*    */   private JSONBeanUtils(String json, Class<T> clz)
/*    */   {
/* 16 */     this.json = json;
/* 17 */     this.clz = clz;
/*    */   }
/*    */ 
/*    */   public static <T> JSONBeanUtils<T> of(String json, Class<T> clz) {
/* 21 */     return new JSONBeanUtils(json, clz);
/*    */   }
/*    */ 
/*    */   public T mapCamel() {
/* 25 */     ExtraProcessor processor = new ExtraProcessor()
/*    */     {
/*    */       public void processExtra(Object obj, String key, Object val) {
/* 28 */         Object t = obj;
/* 29 */         JSONBeanUtils.this.mapField(key, val, (T) t);
/*    */       }
/*    */     };
/* 32 */     return JSON.parseObject(this.json, this.clz, processor, new Feature[0]);
/*    */   }
/*    */ 
/*    */   public T map(final Map<String, String> map) {
/* 36 */     ExtraProcessor processor =new ExtraProcessor()
/*    */     {
/*    */       public void processExtra(Object obj, String key, Object val) {
/* 39 */         Object t = obj;
/* 40 */         if (map.containsKey(key))
/* 41 */            //JSONBeanUtils.this.mapField((String)map.get(key), val, t);
                    JSONBeanUtils.this.mapField(key, val, (T) t);
/*    */       }
/*    */     };
/* 45 */     return JSON.parseObject(this.json, this.clz, processor, new Feature[0]);
/*    */   }
/*    */ 
/*    */   private void mapField(String key, Object val, T t) {
/* 49 */     Method[] methods = t.getClass().getMethods();
/* 50 */     for (Method method : methods) {
/* 51 */       String name = method.getName();
/* 52 */       String field = Utils.toPascal(key);
/* 53 */       if (name.equals("set" + field))
/*    */         try {
/* 55 */           method.invoke(t, new Object[] { JSON.parseObject(JSON.toJSONString(val), method.getParameterTypes()[0]) });
/*    */         } catch (Exception e) {
/* 57 */           e.printStackTrace();
/*    */         }
/*    */     }
/*    */   }
/*    */ }

 