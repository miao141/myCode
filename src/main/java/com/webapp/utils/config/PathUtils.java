/*    */ package com.webapp.utils.config;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public final class PathUtils
/*    */ {
/* 16 */   private static final Logger logger = LoggerFactory.getLogger(PathUtils.class);
/*    */ 
/*    */   public static String getCurPath(Class<?> clz) {
/* 19 */     return encode(clz.getResource("")).toString();
/*    */   }
/*    */ 
/*    */   public static String getUserPath() {
/* 23 */     return System.getProperty("user.dir");
/*    */   }
/*    */ 
/*    */   public static String getClassPath() {
/* 27 */     return encode(getResource("/")).toString();
/*    */   }
/*    */ 
/*    */   public static Path getPath(String path) {
/* 31 */     return encode(getResource(path));
/*    */   }
/*    */ 
/*    */   public static Path getPath(String path, boolean isClasspath) {
/* 35 */     return encode(getResource(path, isClasspath));
/*    */   }
/*    */ 
/*    */   public static boolean isExist(String path) {
/* 39 */     return Files.exists(Paths.get(getUserPath() + path, new String[0]), new LinkOption[0]);
/*    */   }
/*    */ 
/*    */   public static URL getResource(String path) {
/* 43 */     return getResource(path, true);
/*    */   }
/*    */ 
/*    */   public static URL getResource(String path, boolean isClasspath)
/*    */   {
/* 48 */     URL result = null;
/* 49 */     if (isClasspath) {
/* 50 */       result = PathUtils.class.getResource(path);
/* 51 */       if ((result == null) && (!path.contains("/"))) {
/* 52 */         result = PathUtils.class.getResource("/" + path);
/*    */       }
/* 54 */       return result;
/*    */     }
/*    */     try {
/* 57 */       result = Paths.get(path, new String[0]).toAbsolutePath().toUri().toURL();
/*    */     } catch (MalformedURLException e) {
/* 59 */       e.printStackTrace();
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   private static Path encode(URI url)
/*    */   {
/* 67 */     return Paths.get(url);
/*    */   }
/*    */ 
/*    */   private static Path encode(URL url) {
/*    */     try {
/* 72 */       return encode(url.toURI());
/*    */     } catch (URISyntaxException e) {
/* 74 */       logger.error(PathUtils.class.getSimpleName() + " URL转换URI出错", e);
/* 75 */     }throw new RuntimeException(PathUtils.class.getSimpleName() + " URL转换URI出错");
/*    */   }
/*    */ 
/*    */   public static String getJavaPath()
/*    */   {
/* 82 */     return System.getProperty("sun.boot.library.path");
/*    */   }
/*    */ 
/*    */   public static String getUserHome() {
/* 86 */     return System.getProperty("user.home");
/*    */   }
/*    */ 
/*    */   public static String getJavaVersion() {
/* 90 */     return System.getProperty("java.runtime.version");
/*    */   }
/*    */ }

