/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.webapp.utils.format.FmtUtils
 *  org.apache.commons.lang3.RandomStringUtils
 */
package com.webapp.utils.random;

import com.webapp.utils.format.FmtUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtils {
    private RandomUtils() {
    }

    public static void jj() {
    }

    public static int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int nextInt(int least, int bound) {
        return ThreadLocalRandom.current().nextInt(least, bound);
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double nextDouble(int scale) {
        return Double.parseDouble(FmtUtils.of((Object)RandomUtils.nextDouble()).fmt(scale));
    }

    public static double nextDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }

    public static double nextDouble(double bound, int scale) {
        return Double.parseDouble(FmtUtils.of((Object)RandomUtils.nextDouble(bound)).fmt(scale));
    }

    public static double nextDouble(double least, double bound) {
        return ThreadLocalRandom.current().nextDouble(least, bound);
    }

    public static double nextDouble(double least, double bound, int scale) {
        return Double.parseDouble(FmtUtils.of((Object)RandomUtils.nextDouble(least, bound)).fmt(scale));
    }

    public static long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public static long nextLong(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }

    public static long nextLong(long least, long bound) {
        return ThreadLocalRandom.current().nextLong(least, bound);
    }

    public static float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public static float nextFloat(int scale) {
        return Float.parseFloat(FmtUtils.of((Object)Float.valueOf(RandomUtils.nextFloat())).fmt(scale));
    }

    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static byte[] nextBytes(byte[] bytes) {
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }

    public static String str(int length) {
        return RandomStringUtils.randomAlphabetic((int)length);
    }

    public static String strNum(int length) {
        return RandomStringUtils.randomAlphanumeric((int)length);
    }

    public static String ascii(int length) {
        return RandomStringUtils.randomAscii((int)length);
    }

    public static String[] strArray(int size) {
        return (String[])Stream.generate(() -> RandomUtils.str(RandomUtils.nextInt(5, 10))).limit(size).toArray(x$0 -> new String[x$0]);
    }

    public static int[] intArray(int size, int limit) {
        int[] result = new int[size];
        for (int i = 0; i < size; ++i) {
            result[i] = RandomUtils.nextInt(limit);
        }
        return result;
    }

    public static double[] doubleArray(int size, int scale) {
        double[] result = new double[size];
        for (int i = 0; i < size; ++i) {
            result[i] = RandomUtils.nextDouble(scale);
        }
        return result;
    }

    public static float[] floatArray(int size, int scale) {
        float[] result = new float[size];
        for (int i = 0; i < size; ++i) {
            result[i] = RandomUtils.nextFloat(scale);
        }
        return result;
    }

    public static long[] longArray(int size, int limit) {
        long[] result = new long[size];
        for (int i = 0; i < size; ++i) {
            result[i] = RandomUtils.nextLong(limit);
        }
        return result;
    }

    public static ArrayList<String> arrayList(int size) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            list.add(RandomUtils.str(RandomUtils.nextInt(5, 10)));
        }
        return list;
    }

    public static LinkedList<String> linkedList(int size) {
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < size; ++i) {
            list.add(RandomUtils.str(RandomUtils.nextInt(5, 10)));
        }
        return list;
    }

    public static HashSet<String> hashSet(int size) {
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < size; ++i) {
            set.add(RandomUtils.str(RandomUtils.nextInt(5, 10)));
        }
        return set;
    }

    public static TreeSet<String> treeSet(int size) {
        TreeSet<String> set = new TreeSet<String>();
        for (int i = 0; i < size; ++i) {
            set.add(RandomUtils.str(RandomUtils.nextInt(5, 10)));
        }
        return set;
    }

    public static SortedMap<Integer, String> treeMap(int size) {
        TreeMap<Integer, String> map = new TreeMap<Integer, String>();
        for (int i = 0; i < size; ++i) {
            map.put(size - i, RandomUtils.str(RandomUtils.nextInt(5, 10)));
        }
        return map;
    }

    public static HashMap<Integer, String> hashMap(int size) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < size; ++i) {
            map.put(i, RandomUtils.str(RandomUtils.nextInt(5, 10)));
        }
        return map;
    }
}
