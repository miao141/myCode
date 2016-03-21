package com.webapp.utils.regex;

public class RegexConst
{
  public static final String Integer = "^-?[1-9]\\d*|0$";
  public static final String Integer_p = "^[1-9]\\d*|0$";
  public static final String Integer_n = "^-[1-9]\\d*|0$";
  public static final String Num = "^([+-]?)\\d*\\.?\\d+$";
  public static final String Decimal = "^([+-]?)\\d*\\.\\d+$";
  public static final String Decimal_p = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";
  public static final String Decimal_n = "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";
  public static final String Email = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
  public static final String Color = "^[a-fA-F0-9]{6}$";
  public static final String Url = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";
  public static final String Chinese = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
  public static final String Ascii = "^[\\x00-\\xFF]+$";
  public static final String Zipcode = "^\\d{6}$";
  public static final String Mobile = "^(13[0-9]|14[5,7]|15[^4,\\D]|17[6-8]|18[0-9])\\d{8}$";
  public static final String Ip4 = "^((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}";
  public static final String Notempty = "^\\S+$";
  public static final String Picture = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";
  public static final String Rar = "(.*)\\.(rar|zip|7zip|tgz)$";
  public static final String Date = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
  public static final String QQ = "^[1-9]*[1-9][0-9]*$";
  public static final String Tel = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";
  public static final String Letter = "^[A-Za-z]+$";
  public static final String Letter_u = "^[A-Z]+$";
  public static final String Letter_l = "^[a-z]+$";
  public static final String Idcard = "^[1-9]([0-9]{14}|[0-9]{17})$";
}

 