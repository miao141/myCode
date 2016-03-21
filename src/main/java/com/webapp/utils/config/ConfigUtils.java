/*     */ package com.webapp.utils.config;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
import java.util.Iterator;
/*     */ import java.util.Properties;

/*     */ import org.apache.commons.configuration.CompositeConfiguration;
/*     */ import org.apache.commons.configuration.Configuration;
/*     */ import org.apache.commons.configuration.ConfigurationConverter;
/*     */ import org.apache.commons.configuration.ConfigurationException;
/*     */ import org.apache.commons.configuration.PropertiesConfiguration;
/*     */ import org.apache.commons.configuration.SystemConfiguration;
/*     */ import org.apache.commons.configuration.XMLConfiguration;
/*     */ import org.apache.commons.configuration.plist.PropertyListConfiguration;
/*     */ import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public final class ConfigUtils
/*     */ {
/*  27 */   private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
/*     */   private static CompositeConfiguration composite;
/*     */ 
/*     */   public static Properties read(String path)
/*     */   {
/*  31 */     Properties p = new Properties();
/*     */     try { InputStream in = new BufferedInputStream(new FileInputStream(PathUtils.getPath(path).toString())); Throwable localThrowable3 = null;
/*     */       try { p.load(in); }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  32 */         localThrowable3 = localThrowable1; throw localThrowable1;
/*     */       } finally {
/*  34 */         if (in != null) if (localThrowable3 != null) try { in.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else in.close();  
/*     */       } } catch (IOException e) { logger.error(ConfigUtils.class.getSimpleName() + " 读取属性文件出错", e);
/*  36 */       throw new RuntimeException(ConfigUtils.class.getSimpleName() + " 读取属性文件出错");
/*     */     }
/*  38 */     return p;
/*     */   }
/*     */ 
/*     */   public static Properties configConverter(Configuration config) {
/*  42 */     return ConfigurationConverter.getProperties(config);
/*     */   }
/*     */ 
/*     */   public static CompositeConfiguration getConfig() {
/*  46 */     if (composite == null) {
/*  47 */       composite = new CompositeConfiguration();
/*     */     }
/*  49 */     return composite;
/*     */   }
/*     */ 
/*     */   public static CompositeConfiguration addDirConfig(String path, String[] suffix) {
/*  53 */     Path dir = Paths.get(path, new String[0]);
/*  54 */     if ((dir.toString().equals("\\")) || (!Files.isDirectory(dir, new LinkOption[0]))) {
/*  55 */       dir = PathUtils.getPath(path);
/*     */     }
/*  57 */     return addDirConfig(dir, suffix);
/*     */   }
/*     */   public static CompositeConfiguration addDirConfig(Path path, String[] suffix) {
/*     */     try {
/*  61 */       DirectoryStream stream = Files.newDirectoryStream(path); 
                Throwable localThrowable3 = null;
/*     */       try { 
                        for (Iterator iterator = stream.iterator(); iterator.hasNext();) {
                            Path entry = (Path) iterator.next();
                            addConfig(entry.toString());
                        }
                   /* for (Path entry : stream)
  63            addConfig(entry.toString());*/
/*     */       }
/*     */       catch (Throwable localThrowable5)
/*     */       {
/*  61 */         localThrowable3 = localThrowable5; throw localThrowable5;
/*     */       }
/*     */       finally
/*     */       {
/*  65 */         if (stream != null) if (localThrowable3 != null) try { stream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else stream.close();  
/*     */       } } catch (IOException e) { logger.error(" 配置目录 ->" + path + " 加载出错!", e); }
/*     */ 
/*  68 */     return composite;
/*     */   }
/*     */ 
/*     */   public static CompositeConfiguration addConfig(String[] path) {
/*  72 */     int i = 0; for (int k = path.length; i < k; i++) {
/*  73 */       addConfig(path[i]);
/*     */     }
/*  75 */     return composite;
/*     */   }
/*     */ 
/*     */   public static CompositeConfiguration addConfig(String config) {
/*  79 */     addConfig(getConfig(config));
/*  80 */     return composite;
/*     */   }
/*     */ 
/*     */   public static CompositeConfiguration addConfig(Configuration config) {
/*  84 */     if (composite == null) composite = new CompositeConfiguration();
/*  85 */     composite.addConfiguration(config);
/*  86 */     return composite;
/*     */   }
/*     */ 
/*     */   public static CompositeConfiguration addSystemConfig() {
/*  90 */     return addConfig(new SystemConfiguration());
/*     */   }
/*     */ 
/*     */   private static Configuration getConfig(String path) {
/*     */     try {
/*  95 */       if (!Paths.get(path, new String[0]).isAbsolute()) {
/*  96 */         path = PathUtils.getPath(path).toString();
/*     */       }
/*  98 */       if (path.endsWith(".properties")) {
/*  99 */         PropertiesConfiguration config = new PropertiesConfiguration(path);
/* 100 */         config.setReloadingStrategy(getReloading());
/* 101 */         return config;
/* 102 */       }if (path.endsWith(".xml")) {
/* 103 */         XMLConfiguration config = new XMLConfiguration(path);
/* 104 */         config.setReloadingStrategy(getReloading());
/* 105 */         return config;
/* 106 */       }if (path.endsWith(".plist")) {
/* 107 */         PropertyListConfiguration config = new PropertyListConfiguration(path);
/* 108 */         config.setReloadingStrategy(getReloading());
/* 109 */         return config;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ConfigurationException e)
/*     */     {
/* 118 */       logger.error(" 配置文件 ->" + path + " 加载出错!", e);
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   private static FileChangedReloadingStrategy getReloading() {
/* 124 */     FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
/* 125 */     strategy.setRefreshDelay(5000L);
/* 126 */     return strategy;
/*     */   }
/*     */ }

