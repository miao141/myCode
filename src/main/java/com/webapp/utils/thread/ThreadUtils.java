/*     */ package com.webapp.utils.thread;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.CompletionService;
/*     */ import java.util.concurrent.CyclicBarrier;
/*     */ import java.util.concurrent.ExecutorCompletionService;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public abstract interface ThreadUtils
/*     */ {
/*  18 */   public static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);
/*  19 */   public static final AtomicInteger threadCount = new AtomicInteger();
/*  20 */   public static final ExecutorService service = Executors.newCachedThreadPool();
/*  21 */   public static final CompletionService<Integer> completionService = new ExecutorCompletionService(service);
/*     */ 
/*     */   public static <T extends Runnable> void testMultiCase(Supplier<T> run, int count, boolean isNanoTime)
/*     */   {
/*  25 */     long startTime = isNanoTime ? System.nanoTime() : System.currentTimeMillis();
/*     */ 
/*  27 */     for (int i = 0; i < count; i++) {
/*  28 */       Runnable t = (Runnable)run.get();
/*  29 */       completionService.submit(t, Integer.valueOf(i));
/*     */     }
/*     */ 
/*  32 */     for (int i = 0; i < count; i++) {
/*     */       try {
/*  34 */         completionService.take().get();
/*     */       } catch (Exception e) {
/*  36 */         logger.error("获取线程结果出错", e);
/*     */       }
/*     */     }
/*  39 */     computeTime(startTime, isNanoTime);
/*     */   }
/*     */ 
/*     */   public static <T extends Runnable> void testSimpleCase(T run, int count, boolean isNanoTime)
/*     */   {
/*  44 */     long startTime = isNanoTime ? System.nanoTime() : System.currentTimeMillis();
/*     */ 
/*  46 */     for (int i = 0; i < count; i++) {
/*  47 */       completionService.submit(run, Integer.valueOf(i));
/*     */     }
/*  49 */     for (int i = 0; i < count; i++) {
/*     */       try {
/*  51 */         completionService.take().get();
/*     */       } catch (Exception e) {
/*  53 */         logger.error("获取线程结果出错", e);
/*     */       }
/*     */     }
/*  56 */     computeTime(startTime, isNanoTime);
/*     */   }
/*     */ 
/*     */   public static <T extends Runnable> void testMultiCase(Supplier<T> run, int count) {
/*  60 */     testMultiCase(run, count, false);
/*     */   }
/*     */   public static <T extends Runnable> void testSimpleCase(T run, int count) {
/*  63 */     testSimpleCase(run, count, false);
/*     */   }
/*     */ 
/*     */   public static void testCAP(Consumer<Integer> cap, int loop, String name)
/*     */   {
/*  68 */     System.out.println(name);
/*  69 */     long startTime = System.nanoTime();
/*  70 */     for (int i = 0; i < loop; i++) {
/*  71 */       cap.accept(Integer.valueOf(i));
/*     */     }
/*  73 */     computeTime(startTime, true);
/*     */   }
/*     */ 
/*     */   public static void testSimpleCAP(Consumer<Integer> cap, int loop) {
/*  77 */     long startTime = System.nanoTime();
/*  78 */     cap.accept(Integer.valueOf(loop));
/*  79 */     computeTime(startTime, true);
/*     */   }
/*     */ 
/*     */   public static void computeTime(long startTime, boolean isNanoTime) {
/*  83 */     if ((isNanoTime) && (String.valueOf(startTime).length() <= 13)) {
/*  84 */       System.out.println(startTime + "不是正确的纳秒数");
/*  85 */       return;
/*     */     }
/*  87 */     long endTime = isNanoTime ? System.nanoTime() : System.currentTimeMillis();
/*  88 */     long total = endTime - startTime;
/*  89 */     if (isNanoTime) {
/*  90 */       System.out.printf("total time = %s微秒\t", new Object[] { Long.valueOf(total / 1000L) });
/*  91 */       System.out.printf("\ntotal time = %s毫秒\t", new Object[] { Long.valueOf(total / 1000000L) });
/*  92 */       System.out.printf("\ntotal time = %s秒\t", new Object[] { Long.valueOf(total / 1000000000L) });
/*  93 */       System.out.printf("\ntotal time = %s分\t", new Object[] { Long.valueOf(total / 60000000000L) });
/*     */     } else {
/*  95 */       System.out.printf("total time = %s毫秒\t", new Object[] { Long.valueOf(total) });
/*  96 */       System.out.printf("\ntotal time = %s秒\t", new Object[] { Long.valueOf(total / 1000L) });
/*  97 */       System.out.printf("\ntotal time = %s分\t", new Object[] { Long.valueOf(total / 1000L / 60L) });
/*     */     }
/*  99 */     System.out.println();
/* 100 */     System.out.println();
/*     */   }
/*     */ 
/*     */   public static void computeTime(long startTime) {
/* 104 */     computeTime(startTime, false);
/*     */   }
/*     */ 
/*     */   public static void logCyclicBarrier(CyclicBarrier cb, String place) {
/* 108 */     int arrive = cb.getNumberWaiting() + 1;
/*     */ 
/* 110 */     String name = Thread.currentThread().getName();
/* 111 */     System.out.printf("%s已经到达集合点%s, 当前有%s个已经到达\t", new Object[] { name, place, Integer.valueOf(arrive) });
/*     */   }
/*     */ 
/*     */   public static void logAcquire(Semaphore sp) throws InterruptedException {
/* 115 */     System.out.printf("tryAcquire=s, availablePermits=%s, queueLength=%s, queuedThreads=%s, isFair=%s\t", new Object[] { 
/* 116 */       Integer.valueOf(sp
/* 116 */       .availablePermits()), Integer.valueOf(sp.getQueueLength()), 
/* 117 */       Boolean.valueOf(sp
/* 117 */       .hasQueuedThreads()), Boolean.valueOf(sp.isFair()) });
/* 118 */     sp.acquire();
/* 119 */     System.out.printf("线程 %s进入，当前有%d个并发\t", new Object[] { Thread.currentThread().getName(), Integer.valueOf(threadCount.incrementAndGet()) });
/*     */   }
/*     */ 
/*     */   public static void logRelease(Semaphore sp) {
/* 123 */     sp.release();
/* 124 */     System.out.printf("线程 %s离开，当前有%d个并发\t", new Object[] { Thread.currentThread().getName(), Integer.valueOf(threadCount.decrementAndGet()) });
/*     */   }
/*     */ }

