/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.math.NumberUtils
 */
package com.webapp.utils.format;

import java.beans.Beans;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import org.apache.commons.lang3.math.NumberUtils;

public final class FmtUtils {
    private String data;
    private boolean endZero = true;
    private static final ThreadLocal<FmtUtils> local = new ThreadLocal();

    private FmtUtils(String data) {
        this.data = data;
    }

    private FmtUtils setData(String data, boolean endZero) {
        this.data = data;
        this.endZero = endZero;
        return this;
    }

    private static FmtUtils localData(String result) {
        if (local.get() == null) {
            local.set(new FmtUtils(result));
        }
        return local.get().setData(result, true);
    }

    public static <T> FmtUtils of(T data) {
        String result;
        if (Beans.isInstanceOf(data, Number.class)) {
            result = data.toString();
        } else if (Beans.isInstanceOf(data, String.class)) {
            result = String.valueOf(data).replaceAll("(?<!\\d)(\\.)|[^\\d\\.]", "");
        } else {
            throw new IllegalArgumentException("Parameter should be Number or String");
        }
        return FmtUtils.localData(result);
    }

    public String fmt(int scale) {
        return this.fmt(this.getPattern(this.data), scale);
    }

    public String fmt(String pattern) {
        return this.fmt(pattern, -1);
    }

    public String fmtCurrency() {
        return this.fmtCurrency(-1);
    }

    public String fmtCurrency(int scale) {
        return this.fmt(this.getPattern(this.data, "\uffe5"), scale);
    }

    public String fmtPercent() {
        return this.fmtPercent(-1);
    }

    public String fmtPercent(int scale) {
        return this.fmt(this.getPattern(this.data, "", "%"), scale);
    }

    public String fmtRMB() {
        return this.fmtRMB(-1);
    }

    public String fmtRMB(int scale) {
        return this.fmt(this.getPattern(this.data, "RMB"), scale);
    }

    public String fmtDollar() {
        return this.fmtDollar(-1);
    }

    public String fmtDollar(int scale) {
        return this.fmt(this.getPattern(this.data, "$"), scale);
    }

    public int toInt() {
        return NumberUtils.toInt((String)this.data);
    }

    public double toDouble() {
        return NumberUtils.toDouble((String)this.data);
    }

    public double toDouble(int scale) {
        return NumberUtils.toDouble((String)this.fmt(scale));
    }

    public float toFloat() {
        return NumberUtils.toFloat((String)this.data);
    }

    public float toFloat(int scale) {
        return NumberUtils.toFloat((String)this.fmt(scale));
    }

    public long toLong() {
        return NumberUtils.toLong((String)this.data);
    }

    public short toShort() {
        return NumberUtils.toShort((String)this.data);
    }

    public FmtUtils endZero(boolean endZero) {
        this.endZero = endZero;
        return this;
    }

    private String fmt(String pattern, int scale) {
        DecimalFormat format = new DecimalFormat();
        format.setRoundingMode(RoundingMode.HALF_UP);
        Number parse = null;
        try {
            parse = format.parse(this.data);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        format.applyPattern(pattern);
        if (scale > 0) {
            format.setMaximumFractionDigits(scale);
            format.setMinimumFractionDigits(scale);
        }
        return format.format(parse);
    }

    private String getPattern(String data) {
        return this.getPattern(data, "", "");
    }

    private String getPattern(String data, String before) {
        return this.getPattern(data, before, "");
    }

    private String getPattern(String data, String before, String after) {
        int length = data.replaceFirst("\\d+\\.|\\d+", "").length();
        String symbol = this.endZero ? "0" : "#";
        StringBuffer pattern = new StringBuffer(before + "0");
        pattern.append(length > 0 ? "." : "");
        for (int i = 0; i < length; ++i) {
            pattern.append(symbol);
        }
        pattern.append(after);
        return pattern.toString();
    }
}
