/*     */ package com.webapp.utils.string;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class IDUtils
/*     */ {
/*  17 */   private static final int[] checkCodes = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
/*     */   private ID id;
/*  19 */   private List<String> errorMsgs = new ArrayList();
/*  20 */   private String addr = "";
/*     */ 
/*     */   public static IDUtils of(String num) {
/*  23 */     return new IDUtils(num);
/*     */   }
/*     */ 
/*     */   private IDUtils(String num)
/*     */   {
/*  30 */     this.id = new ID(num.length() >= 18 ? num : "0000000000000000000");
/*     */   }
/*     */ 
/*     */   public String getBirth()
/*     */   {
/*  38 */     return this.id.getBirth().substring(0, 4) + "年" + this.id.getBirth().substring(4, 6) + "月" + this.id.getBirth().substring(6, 8) + "日";
/*     */   }
/*     */ 
/*     */   public String getSex()
/*     */   {
/*  46 */     return this.id.getSeq().charAt(2) % '\002' == 0 ? "女" : "男";
/*     */   }
/*     */ 
/*     */   public String getAddr()
/*     */   {
/*  54 */     if ("".equals(this.addr)) {
/*  55 */       checkAddr();
/*     */     }
/*  57 */     return this.addr;
/*     */   }
/*     */ 
/*     */   public boolean checkAll()
/*     */   {
/*  67 */     if (!checkLength()) {
/*  68 */       return false;
/*     */     }
/*  70 */     return (checkBirth() & checkCode() & checkAddr());
/*     */   }
/*     */ 
/*     */   public boolean checkLength()
/*     */   {
/*  78 */     int length = this.id.getNum().length();
/*  79 */     if (length == 18) {
/*  80 */       return true;
/*     */     }
/*  82 */     this.errorMsgs.add("身份证长度不正确");
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean checkBirth()
/*     */   {
                    int day;
    /*     */     int month;
    /*     */     int year;
/*  91 */     String birth = this.id.getBirth();
/*     */     try
/*     */     {
/*  94 */       year = Integer.valueOf(birth.substring(0, 4)).intValue();
/*  95 */         month = Integer.valueOf(birth.substring(4, 6)).intValue();
/*  96 */        day = Integer.valueOf(birth.substring(6, 8)).intValue();
/*     */     }
/*     */     catch (NumberFormatException e)
/*     */     {
/*     */      
/*  98 */       this.errorMsgs.add("身份证生日码不正确！");
/*  99 */       return false;
/*     */     }
/*     */     
/* 101 */     if ((year >= 1900) && (year <= 2010) && (month >= 1) && (month <= 12) && (day >= 1) && (day <= 31)) {
/* 102 */       return true;
/*     */     }
/* 104 */     this.errorMsgs.add("身份证生日码不正确！");
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean checkAddr()
/*     */   {
/* 113 */     String addrCode = this.id.getAddr();
/* 114 */     this.addr = readAddress(addrCode);
/* 115 */     if (this.addr == null) {
/* 116 */       this.errorMsgs.add("身份证地址码不正确！");
/* 117 */       return false;
/*     */     }
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean checkCode()
/*     */   {
/* 127 */     String chCode = caculateCheckCode(this.id.getNum());
/* 128 */     if (!this.id.getCheck().equalsIgnoreCase(chCode)) {
/* 129 */       this.errorMsgs.add("身份证校验码不正确, 正确的校验码是 " + chCode);
/* 130 */       return false;
/*     */     }
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   public String getErrorMsg()
/*     */   {
/* 140 */     return this.errorMsgs.size() > 0 ? (String)this.errorMsgs.get(0) : "";
/*     */   }
/*     */ 
/*     */   public List<String> getErrorMsgs()
/*     */   {
/* 148 */     return this.errorMsgs;
/*     */   }
/*     */ 
/*     */   public static final int[] toInt(String num)
/*     */   {
/* 156 */     int[] ins = new int[18];
/* 157 */     for (int i = 0;  i < ins.length - 1; i++) {
/* 159 */       ins[i] = Integer.valueOf(num.substring(i, i + 1)).intValue();
    /*     */    
    /* 161 */   String last = num.substring(i, i + 1);
    /* 162 */   ins[i] = ("X".equals(last) ? 10 : Integer.valueOf(last).intValue());
              }
/* 163 */     return ins;
/*     */   }
/*     */ 
/*     */   private static final String caculateCheckCode(String num)
/*     */   {
/* 171 */     int total = 0;
/* 172 */     int[] ins = new int[18];
/*     */     try {
/* 174 */       for (int i = 0; i < ins.length - 1; i++) {
/* 175 */         ins[i] = Integer.valueOf(num.substring(i, i + 1)).intValue();
/* 176 */         total += ins[i] * checkCodes[i];
/*     */       }
/*     */     } catch (NumberFormatException e) {
/* 179 */       return null;
/*     */     }
/* 181 */     int modResult = total % 11;
/*     */ 
/* 183 */     String result = null;
/*     */ 
/* 185 */     switch (modResult) {
/*     */     case 0:
/* 187 */       result = "1";
/* 188 */       break;
/*     */     case 1:
/* 190 */       result = "0";
/* 191 */       break;
/*     */     case 2:
/* 193 */       result = "X";
/* 194 */       break;
/*     */     case 3:
/* 196 */       result = "9";
/* 197 */       break;
/*     */     case 4:
/* 199 */       result = "8";
/* 200 */       break;
/*     */     case 5:
/* 202 */       result = "7";
/* 203 */       break;
/*     */     case 6:
/* 205 */       result = "6";
/* 206 */       break;
/*     */     case 7:
/* 208 */       result = "5";
/* 209 */       break;
/*     */     case 8:
/* 211 */       result = "4";
/* 212 */       break;
/*     */     case 9:
/* 214 */       result = "3";
/* 215 */       break;
/*     */     case 10:
/* 217 */       result = "2";
/* 218 */       break;
/*     */     }
/*     */ 
/* 222 */     return result;
/*     */   }
/*     */ 
/*     */   private static final String readAddress(String addrNum)
/*     */   {
/* 231 */     char first = addrNum.charAt(0);
/* 232 */     if ((first == '1') || (first == '2') || (first == '3') || (first == '4') || (first == '5') || (first == '6')) {
/* 233 */       String filePath = "/address/" + first + ".dic";
/* 234 */       String addr = readAddress(filePath, "UTF-8", addrNum);
/* 235 */       return addr;
/*     */     }
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   private static final String readAddress(String filePath, String charset, String addrNum)
/*     */   {
/* 248 */     String addr = null;
/*     */     try {
/* 250 */       InputStream is = IDUtils.class.getResourceAsStream(filePath);
/* 251 */       BufferedReader buffReader = new BufferedReader(new InputStreamReader(is, charset));
/*     */       String line;
/* 254 */       while ((line = buffReader.readLine()) != null) {
/* 255 */         if (addrNum.equals(line.substring(0, 6))) {
/* 256 */           addr = line.substring(7, line.length());
/*     */         }
/*     */       }
/*     */ 
/* 260 */       buffReader.close();
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 263 */       System.err.println("找到不地址码文件");
/* 264 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 266 */       System.err.println("读取地址码文件失败");
/* 267 */       e.printStackTrace();
/*     */     }
/* 269 */     return addr;
/*     */   }
/*     */   private class ID { private String num;
/*     */     private String addr;
/*     */     private String birth;
/*     */     private String seq;
/*     */     private String check;
/*     */ 
/* 281 */     public ID(String num) { this.num = num;
/* 282 */       this.addr = num.substring(0, 6);
/* 283 */       this.birth = num.substring(6, 14);
/* 284 */       this.seq = num.substring(14, 17);
/* 285 */       this.check = num.substring(17, 18); }
/*     */ 
/*     */     public String getAddr()
/*     */     {
/* 289 */       return this.addr;
/*     */     }
/*     */     public String getBirth() {
/* 292 */       return this.birth;
/*     */     }
/*     */     public String getSeq() {
/* 295 */       return this.seq;
/*     */     }
/*     */     public String getCheck() {
/* 298 */       return this.check;
/*     */     }
/*     */     public String getNum() {
/* 301 */       return this.num;
/*     */     }
/*     */   }
/*     */ }

 