/*
 * Decompiled with CFR 0_115.
 */
package com.webapp.utils.jpinyin;

import com.webapp.utils.jpinyin.ChineseHelper;
import com.webapp.utils.jpinyin.PinyinFormat;
import com.webapp.utils.jpinyin.PinyinResource;
import java.util.LinkedHashSet;
import java.util.Properties;

public final class PinyinHelper {
    private static final Properties PINYIN_TABLE = PinyinResource.getPinyinTable();
    private static final Properties MUTIL_PINYIN_TABLE = PinyinResource.getMutilPintinTable();
    private static final String PINYIN_SEPARATOR = ",";
    private static final String ALL_UNMARKED_VOWEL = "aeiouv";
    private static final String ALL_MARKED_VOWEL = "\u0101\u00e1\u01ce\u00e0\u0113\u00e9\u011b\u00e8\u012b\u00ed\u01d0\u00ec\u014d\u00f3\u01d2\u00f2\u016b\u00fa\u01d4\u00f9\u01d6\u01d8\u01da\u01dc";

    private static String[] convertWithToneNumber(String pinyinArrayString) {
        String[] pinyinArray = pinyinArrayString.split(",");
        for (int i = pinyinArray.length - 1; i >= 0; --i) {
            boolean hasMarkedChar = false;
            String originalPinyin = pinyinArray[i].replaceAll("\u00fc", "v");
            for (int j = originalPinyin.length() - 1; j >= 0; --j) {
                char originalChar = originalPinyin.charAt(j);
                if (originalChar >= 'a' && originalChar <= 'z') continue;
                int indexInAllMarked = "\u0101\u00e1\u01ce\u00e0\u0113\u00e9\u011b\u00e8\u012b\u00ed\u01d0\u00ec\u014d\u00f3\u01d2\u00f2\u016b\u00fa\u01d4\u00f9\u01d6\u01d8\u01da\u01dc".indexOf(originalChar);
                int toneNumber = indexInAllMarked % 4 + 1;
                char replaceChar = "aeiouv".charAt((indexInAllMarked - indexInAllMarked % 4) / 4);
                pinyinArray[i] = originalPinyin.replaceAll(String.valueOf(originalChar), String.valueOf(replaceChar)) + toneNumber;
                hasMarkedChar = true;
                break;
            }
            if (hasMarkedChar) continue;
            pinyinArray[i] = originalPinyin + "5";
        }
        return pinyinArray;
    }

    private static String[] convertWithoutTone(String pinyinArrayString) {
        for (int i = "\u0101\u00e1\u01ce\u00e0\u0113\u00e9\u011b\u00e8\u012b\u00ed\u01d0\u00ec\u014d\u00f3\u01d2\u00f2\u016b\u00fa\u01d4\u00f9\u01d6\u01d8\u01da\u01dc".length() - 1; i >= 0; --i) {
            char originalChar = "\u0101\u00e1\u01ce\u00e0\u0113\u00e9\u011b\u00e8\u012b\u00ed\u01d0\u00ec\u014d\u00f3\u01d2\u00f2\u016b\u00fa\u01d4\u00f9\u01d6\u01d8\u01da\u01dc".charAt(i);
            int replaceChar = "aeiouv".charAt((i - i % 4) / 4);
            pinyinArrayString = pinyinArrayString.replaceAll(String.valueOf(originalChar), String.valueOf((char)replaceChar));
        }
        String[] pinyinArray = pinyinArrayString.replaceAll("\u00fc", "v").split(",");
        LinkedHashSet<String> pinyinSet = new LinkedHashSet<String>();
        for (String pinyin : pinyinArray) {
            pinyinSet.add(pinyin);
        }
        return pinyinSet.toArray(new String[pinyinSet.size()]);
    }

    private static String[] formatPinyin(String pinyinString, PinyinFormat pinyinFormat) {
        if (pinyinFormat == PinyinFormat.WITH_TONE_MARK) {
            return pinyinString.split(",");
        }
        if (pinyinFormat == PinyinFormat.WITH_TONE_NUMBER) {
            return PinyinHelper.convertWithToneNumber(pinyinString);
        }
        if (pinyinFormat == PinyinFormat.WITHOUT_TONE) {
            return PinyinHelper.convertWithoutTone(pinyinString);
        }
        return null;
    }

    public static String[] convertToPinyinArray(char c, PinyinFormat pinyinFormat) {
        String pinyin = PINYIN_TABLE.getProperty(String.valueOf(c));
        if (pinyin != null && !pinyin.equals("null")) {
            return PinyinHelper.formatPinyin(pinyin, pinyinFormat);
        }
        return null;
    }

    public static String[] convertToPinyinArray(char c) {
        return PinyinHelper.convertToPinyinArray(c, PinyinFormat.WITH_TONE_MARK);
    }

    public static String convertToPinyinString(String str, String separator, PinyinFormat pinyinFormat) {
        str = ChineseHelper.convertToSimplifiedChinese(str);
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            if (ChineseHelper.isChinese(c) || c == '\u3007') {
                int rightIndex;
                boolean isFoundFlag = false;
                int rightMove = 3;
                int n = rightIndex = i + rightMove < len ? i + rightMove : len - 1;
                while (rightIndex > i) {
                    String cizu = str.substring(i, rightIndex + 1);
                    if (MUTIL_PINYIN_TABLE.containsKey(cizu)) {
                        String[] pinyinArray = PinyinHelper.formatPinyin(MUTIL_PINYIN_TABLE.getProperty(cizu), pinyinFormat);
                        int l = pinyinArray.length;
                        for (int j = 0; j < l; ++j) {
                            sb.append(pinyinArray[j]);
                            if (j >= l - 1) continue;
                            sb.append(separator);
                        }
                        i = rightIndex;
                        isFoundFlag = true;
                        break;
                    }
                    --rightIndex;
                }
                if (!isFoundFlag) {
                    String[] pinyinArray = PinyinHelper.convertToPinyinArray(str.charAt(i), pinyinFormat);
                    if (pinyinArray != null) {
                        sb.append(pinyinArray[0]);
                    } else {
                        sb.append(str.charAt(i));
                    }
                }
                if (i >= len - 1) continue;
                sb.append(separator);
                continue;
            }
            sb.append(c);
            if (i + 1 >= len || !ChineseHelper.isChinese(str.charAt(i + 1))) continue;
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String convertToPinyinString(String str, String separator) {
        return PinyinHelper.convertToPinyinString(str, separator, PinyinFormat.WITH_TONE_MARK);
    }

    public static boolean hasMultiPinyin(char c) {
        String[] pinyinArray = PinyinHelper.convertToPinyinArray(c);
        if (pinyinArray != null && pinyinArray.length > 1) {
            return true;
        }
        return false;
    }

    public static String getShortPinyin(String str) {
        String separator = "#";
        StringBuilder sb = new StringBuilder();
        char[] charArray = new char[str.length()];
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            String[] pinyinArray;
            char c = str.charAt(i);
            if (!ChineseHelper.isChinese(c) && c != '\u3007') {
                charArray[i] = c;
                continue;
            }
            sb.append(c);
            for (int j = i + 1; j < len && (ChineseHelper.isChinese(str.charAt(j)) || str.charAt(j) == '\u3007'); ++j) {
                sb.append(str.charAt(j));
            }
            String hanziPinyin = PinyinHelper.convertToPinyinString(sb.toString(), separator, PinyinFormat.WITHOUT_TONE);
            for (String string : pinyinArray = hanziPinyin.split(separator)) {
                charArray[i] = string.charAt(0);
                ++i;
            }
            --i;
            sb.delete(0, sb.toString().length());
            sb.trimToSize();
        }
        return String.valueOf(charArray);
    }
}
