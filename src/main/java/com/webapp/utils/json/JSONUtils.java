/*     */ package com.webapp.utils.json;
/*     */ 
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.serializer.AfterFilter;
/*     */ import com.alibaba.fastjson.serializer.BeforeFilter;
/*     */ import com.alibaba.fastjson.serializer.JSONSerializer;
/*     */ import com.alibaba.fastjson.serializer.NameFilter;
/*     */ import com.alibaba.fastjson.serializer.PascalNameFilter;
/*     */ import com.alibaba.fastjson.serializer.PropertyFilter;
/*     */ import com.alibaba.fastjson.serializer.PropertyPreFilter;
/*     */ import com.alibaba.fastjson.serializer.SerializeConfig;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
/*     */ import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
/*     */ import com.alibaba.fastjson.serializer.ValueFilter;
/*     */ import com.webapp.utils.string.Utils;
/*     */ import java.lang.reflect.Field;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class JSONUtils
/*     */ {
/*     */   private Object jsonObj;
/*     */   private JSONSerializer jsonSerializer;
/*  36 */   private static final ThreadLocal<JSONUtils> local = new ThreadLocal();
/*     */ 
/*     */   private JSONUtils(Object object) {
/*  39 */     this.jsonObj = object;
/*  40 */     this.jsonSerializer = new JSONSerializer(new SerializeConfig());
/*     */   }
/*     */ 
/*     */   private JSONUtils setData(Object jsonObj, JSONSerializer jsonSerializer) {
/*  44 */     this.jsonObj = jsonObj;
/*  45 */     this.jsonSerializer = jsonSerializer;
/*  46 */     return this;
/*     */   }
/*     */ 
/*     */   private static JSONUtils localData(Object object) {
/*  50 */     if (local.get() == null) local.set(new JSONUtils(object));
/*  51 */     return ((JSONUtils)local.get()).setData(object, new JSONSerializer(new SerializeConfig()));
/*     */   }
/*     */ 
/*     */   public static JSONUtils of(Object object) {
/*  55 */     return localData(object);
/*     */   }
/*     */ 
/*     */   public static String toString(Object object)
/*     */   {
/*  65 */     return JSON.toJSONString(object);
/*     */   }
/*     */ 
/*     */   public String toCamelKey()
/*     */   {
/*  76 */     this.jsonSerializer.getNameFilters().add(new NameFilter() {
/*     */       public String process(Object object, String name, Object value) {
/*  78 */         return Utils.toCamel(name);
/*     */       }
/*     */     });
/*  81 */     return toString();
/*     */   }
/*     */ 
/*     */   public String toPascalKey()
/*     */   {
/*  92 */     this.jsonSerializer.getNameFilters().add(new PascalNameFilter());
/*  93 */     return toString();
/*     */   }
/*     */ 
/*     */   public String toSnakeKey()
/*     */   {
/* 104 */     this.jsonSerializer.getNameFilters().add(new NameFilter() {
/*     */       public String process(Object object, String name, Object value) {
/* 106 */         return Utils.toSnake(name);
/*     */       }
/*     */     });
/* 109 */     return toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     this.jsonSerializer.write(this.jsonObj);
/* 117 */     return this.jsonSerializer.toString();
/*     */   }
/*     */ 
/*     */   public String toPrettyString()
/*     */   {
/* 124 */     this.jsonSerializer.config(SerializerFeature.PrettyFormat, true);
/* 125 */     return toString();
/*     */   }
/*     */ 
/*     */   public String toBeanToArray()
/*     */   {
/* 136 */     this.jsonSerializer.config(SerializerFeature.BeanToArray, true);
/* 137 */     return toString();
/*     */   }
/*     */ 
/*     */   public JSONUtils before(final String key, final Object value)
/*     */   {
/* 148 */     this.jsonSerializer.getBeforeFilters().add(new BeforeFilter() {
/*     */       public void writeBefore(Object object) {
/* 150 */         writeKeyValue(key, value);
/*     */       }
/*     */     });
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils after(final String key, final Object value)
/*     */   {
/* 164 */     this.jsonSerializer.getAfterFilters().add(new AfterFilter() {
/*     */       public void writeAfter(Object object) {
/* 166 */         writeKeyValue(key, value);
/*     */       }
/*     */     });
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils include(String[] includes)
/*     */   {
/* 180 */     SimplePropertyPreFilter filter = new SimplePropertyPreFilter(includes);
/* 181 */     this.jsonSerializer.getPropertyPreFilters().add(filter);
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils exclude(String[] excludes)
/*     */   {
/* 193 */     SimplePropertyPreFilter filter = new SimplePropertyPreFilter(new String[0]);
/* 194 */     filter.getExcludes().addAll(Arrays.asList(excludes));
/* 195 */     this.jsonSerializer.getPropertyPreFilters().add(filter);
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils modifyKey(final String key, final String replacement)
/*     */   {
/* 207 */     this.jsonSerializer.getNameFilters().add(new NameFilter() {
/*     */       public String process(Object object, String name, Object value) {
/* 209 */         return key.equals(name) ? replacement : name;
/*     */       }
/*     */     });
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils modifyVal(final String key, final Object replacement)
/*     */   {
/* 223 */     this.jsonSerializer.getValueFilters().add(new ValueFilter() {
/* 224 */       String orgKey = key;
/* 225 */       List<NameFilter> nameFilters = JSONUtils.this.jsonSerializer.getNameFilters();
/*     */ 
/* 227 */       public Object process(Object object, String name, Object value) { for (NameFilter filter : this.nameFilters) {
/* 228 */           this.orgKey = filter.process(object, this.orgKey, value);
/*     */         }
/* 230 */         return this.orgKey.equals(name) ? replacement : value;
/*     */       }
/*     */     });
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils modifyVal(final String key, final String regexVal, final String replacement)
/*     */   {
/* 244 */     this.jsonSerializer.getValueFilters().add(new ValueFilter() {
/* 245 */       String orgKey = key;
/* 246 */       List<NameFilter> nameFilters = JSONUtils.this.jsonSerializer.getNameFilters();
/*     */ 
/* 248 */       public Object process(Object object, String name, Object value) { for (NameFilter filter : this.nameFilters) {
/* 249 */           this.orgKey = filter.process(object, this.orgKey, value);
/*     */         }
/* 251 */         if ((this.orgKey.equals(name)) && (String.valueOf(value).matches(regexVal))) {
/* 252 */           value = replacement;
/*     */         }
/* 254 */         return value;
/*     */       }
/*     */     });
/* 257 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils filterVal(final String key, final String regexVal)
/*     */   {
/* 271 */     PropertyFilter filter = new PropertyFilter() {
/*     */       public boolean apply(Object object, String name, Object value) {
/* 273 */         return key.equals(name) ? String.valueOf(value).matches(regexVal) : true;
/*     */       }
/*     */     };
/* 276 */     this.jsonSerializer.getPropertyFilters().add(filter);
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils dateFormat(String format)
/*     */   {
/* 286 */     this.jsonSerializer.getMapping().put(Date.class, new SimpleDateFormatSerializer(format));
/*     */ 
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   public <T> JSONUtils doubleFormat(final String pattern, final Class<T> clz) {
/* 292 */     this.jsonSerializer.getValueFilters().add(new ValueFilter(){
                    DecimalFormat format;
                    List<NameFilter> nameFilters;
                
                    public Object process(Object object, String name, Object value) {
                        Field[] fields;
                        for (Field field : fields = clz.getDeclaredFields()) {
                            if (!field.getType().getSimpleName().equalsIgnoreCase(Double.TYPE.getSimpleName())) continue;
                            String key = field.getName();
                            for (NameFilter filter : this.nameFilters) {
                                key = filter.process((Object)null, key, (Object)null);
                            }
                            if (!key.equals(name)) continue;
                            return this.format.format(value);
                        }
                        return value;
                    }
                });
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toCompatible()
/*     */   {
/* 322 */     this.jsonSerializer.config(SerializerFeature.BrowserCompatible, true);
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toSort() {
/* 327 */     this.jsonSerializer.config(SerializerFeature.SortField, true);
/* 328 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toNullNumAsZero()
/*     */   {
/* 340 */     this.jsonSerializer.config(SerializerFeature.WriteNullNumberAsZero, true);
/* 341 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toNullBoolAsFalse()
/*     */   {
/* 353 */     this.jsonSerializer.config(SerializerFeature.WriteNullBooleanAsFalse, true);
/* 354 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toNullStrAsEmpty()
/*     */   {
/* 366 */     this.jsonSerializer.config(SerializerFeature.WriteNullStringAsEmpty, false);
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toNonStrKeyAsStr() {
/* 371 */     this.jsonSerializer.config(SerializerFeature.WriteNonStringKeyAsString, true);
/* 372 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toMapNullValue()
/*     */   {
/* 384 */     this.jsonSerializer.config(SerializerFeature.WriteMapNullValue, true);
/* 385 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils toNullListAsEmpty()
/*     */   {
/* 397 */     this.jsonSerializer.config(SerializerFeature.WriteNullListAsEmpty, true);
/* 398 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONUtils addNameFilter(NameFilter filter) {
/* 402 */     this.jsonSerializer.getNameFilters().add(filter);
/* 403 */     return this;
/*     */   }
/*     */   public JSONUtils addValueFilters(ValueFilter filter) {
/* 406 */     this.jsonSerializer.getValueFilters().add(filter);
/* 407 */     return this;
/*     */   }
/*     */   public JSONUtils addPropertyFilter(PropertyFilter filter) {
/* 410 */     this.jsonSerializer.getPropertyFilters().add(filter);
/* 411 */     return this;
/*     */   }
/*     */   public JSONUtils addPropertyPreFilter(PropertyPreFilter filter) {
/* 414 */     this.jsonSerializer.getPropertyPreFilters().add(filter);
/* 415 */     return this;
/*     */   }
/*     */ }

 