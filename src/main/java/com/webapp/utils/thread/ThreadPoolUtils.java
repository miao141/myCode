/*     */ package com.webapp.utils.thread;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletionService;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorCompletionService;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public final class ThreadPoolUtils
/*     */ {
/*  22 */   private static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtils.class);
/*     */ 
/*     */   public <T> List<FutureTask<T>> execute(ExecutorService executor, Callable<T> callable, int loopSize)
/*     */   {
/*  26 */     List futureTasks = new ArrayList();
/*  27 */     for (int i = 0; i < loopSize; i++) {
/*  28 */       FutureTask task = new FutureTask(callable);
/*  29 */       executor.submit(task);
/*  30 */       futureTasks.add(task);
/*     */     }
/*  32 */     return futureTasks;
/*     */   }
/*     */ 
/*     */   public <T> List<T> submitResult(ExecutorService executor, Callable<T> callable, int loopSize)
/*     */   {
/*  37 */     List result = new ArrayList();
/*     */ 
/*  39 */     List  futures =  submit4Future(executor, callable, loopSize);
              for (int i = 0; i < futures.size(); i++) {
                result.add(futures.get(i));
              }
 /*     */
/*  47 */     return result;
/*     */   }
/*     */ 
/*     */   public <T> List<Future<T>> submit4Future(ExecutorService executor, Callable<T> callable, int loopSize)
/*     */   {
/*  52 */     List futures = new ArrayList();
/*  53 */     for (int i = 0; i < loopSize; i++) {
/*  54 */       Future future = executor.submit(callable);
/*  55 */       futures.add(future);
/*     */     }
/*  57 */     return futures;
/*     */   }
/*     */ 
/*     */   public <T> CompletionService<T> submit4Complete(ExecutorService executor, Callable<T> callable, int loopSize)
/*     */   {
/*  62 */     CompletionService cs = new ExecutorCompletionService(executor);
/*  63 */     for (int i = 0; i < loopSize; i++) {
/*  64 */       cs.submit(callable);
/*     */     }
/*  66 */     return cs;
/*     */   }
/*     */ 
/*     */   public <T> CompletionService<T> submit4Complete(ExecutorService executor, Runnable runnable, int loopSize, T result)
/*     */   {
/*  71 */     CompletionService cs = new ExecutorCompletionService(executor);
/*  72 */     for (int i = 0; i < loopSize; i++) {
/*  73 */       cs.submit(runnable, result);
/*     */     }
/*  75 */     return cs;
/*     */   }
/*     */ 
/*     */   public static ExecutorService newThreadPool(int minPoolSize, int maxPoolSize, int keepAliveTime, BlockingQueue<Runnable> workQueue)
/*     */   {
/*  81 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(minPoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
/*     */ 
/*  83 */     executor.setCorePoolSize(minPoolSize);
/*     */ 
/*  89 */     executor.setMaximumPoolSize(maxPoolSize);
/*  90 */     executor.setKeepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS);
/*  91 */     executor.allowCoreThreadTimeOut(false);
/*  92 */     executor.setThreadFactory(Executors.defaultThreadFactory());
/*     */ 
/* 101 */     executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
/* 102 */     return executor;
/*     */   }
/*     */ }

