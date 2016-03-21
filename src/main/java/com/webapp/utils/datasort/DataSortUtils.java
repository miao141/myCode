package com.webapp.utils.datasort;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.lang3.ArrayUtils;

public final class DataSortUtils
{
  public static byte[] sort(byte[] data)
  {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static char[] sort(char[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static double[] sort(double[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static float[] sort(float[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static int[] sort(int[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static long[] sort(long[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static short[] sort(short[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static byte[] sort(byte[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Byte[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static char[] sort(char[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Character[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static double[] sort(double[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Double[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static float[] sort(float[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Float[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static int[] sort(int[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Integer[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static long[] sort(long[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Long[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static short[] sort(short[] data, OrderType type) {
    return ArrayUtils.toPrimitive((Short[])getOrderSort(ArrayUtils.toObject(data), type));
  }

  public static <T extends Comparable<T>> T[] sort(T[] data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static <T extends Comparable<T>> T[] sort(T[] data, OrderType type) {
    return getOrderSort(data, type);
  }

  public static <T extends Comparable<T>> List<T> sort(List<T> data) {
    return sort(data, OrderType.binaryInsert_asce);
  }

  public static <T extends Comparable<T>> List<T> sort(List<T> data, OrderType type) {
    return sort(data, type, null);
  }

  public static <T> T[] sort(T[] data, OrderType type, Comparator<T> comp) {
    return getOrderSort(data, type, comp);
  }

  public static <T> List<T> sort(List<T> data, OrderType type, Comparator<T> comp) {
    Object[] tArr = null;
    tArr = data.toArray(tArr);
    getOrderSort((T[])tArr, type, comp);
    ListIterator i = data.listIterator();
    for (int j = 0; j < tArr.length; j++) {
      i.next();
      i.set(tArr[j]);
    }
    return data;
  }

  private static <T> T[] shellSort(T[] data, Comparator<T> comp)
  {
    int length = data.length;
    int h = 1;
    while (h <= length / 3) {
      h = h * 3 + 1;
    }
    while (h > 0) {
      for (int i = h; i < length; i++) {
        Object tmp = data[i];

        if (compare(data[i], data[(i - h)], comp) < 0) {
          int j = i - h;

          for (; (j >= 0) && (compare((T)data[i], (T)tmp, comp) > 0); j -= h) {
            data[(j + h)] = data[j];
          }
          data[(j + h)] = (T) tmp;
        }
      }
      h = (h - 1) / 3;
    }
    return data;
  }

  private static <T> T[] binaryInsertSort(T[] data, Comparator<T> comp)
  {
    for (int i = 1; i < data.length; i++) {
      Object temp = data[i];
      int low = 0;
      int high = i - 1;
      while (low <= high) {
        int mid = low + high >> 1;
        if (compare((T)temp, data[mid], comp) > 0)
          low = mid + 1;
        else {
          high = mid - 1;
        }
      }
      for (int j = i; j > low; j--) {
        data[j] = data[(j - 1)];
      }
      data[low] = (T) temp;
    }
    return data;
  }

  private static <T> T[] insertSort(T[] data, Comparator<T> comp)
  {
  /*  for (int i = 1; i < data.length; i++) {
      Object temp = data[i];

      if (compare(data[i], data[(i - 1)], comp) < 0) {
        for (int j = i - 1; 
          (j >= 0) && (compare(data[j], temp, comp) > 0); j--) {
          data[(j + 1)] = data[j];
        }
        data[(j + 1)] = temp;
      }
    }
    return data;*/
    for (int i = 1; i < data.length; ++i) {
        T temp = data[i];
        if (DataSortUtils.compare(data[i], data[i - 1], comp) >= 0) continue;
        int j = i - 1;
        for (; j >= 0 && DataSortUtils.compare(data[j], temp, comp) > 0; --j) {
            data[j + 1] = data[j];
        }
        data[j + 1] = temp;
    }
    return data;
  }

  private static <T> T[] quickSort(T[] data, Comparator<T> comp)
  {
    return quickSort(data, 0, data.length - 1, comp);
  }

  private static <T> T[] bubbleSort(T[] data, Comparator<T> comp)
  {
    for (int i = 0; i < data.length - 1; i++) {
      boolean flag = false;
      for (int j = 0; j < data.length - 1 - i; j++)
      {
        if (compare(data[j], data[(j + 1)], comp) > 0) {
          swap(data, j, j + 1);
          flag = true;
        }
      }
      if (!flag) break;
    }
    return data;
  }

  private static <T> T[] selectSort(T[] data, Comparator<T> comp)
  {
    int length = data.length;

    for (int i = 0; i < length - 1; i++) {
      int minIndex = i;
      for (int j = i + 1; j < length; j++) {
        if (compare(data[j], data[minIndex], comp) < 0) minIndex = j;
      }
      if (minIndex != i) swap(data, minIndex, i);
    }
    return data;
  }

  private static <T> T[] heapSort(T[] data, Comparator<T> comp)
  {
    int length = data.length;
    for (int i = 0; i < length; i++) {
      buildMaxHeap(data, length - i, comp);
      swap(data, 0, length - i - 1);
    }
    return data;
  }

  private static <T> T[] buildMaxHeap(T[] data, int lastIndex, Comparator<T> comp) {
    int lastNode = (lastIndex >> 1) - 1;
    for (int i = lastNode; i >= 0; i--) {
      int k = i;
      while ((k << 1) + 1 < lastIndex) {
        int bigIndex = (k << 1) + 1;
        if (bigIndex < lastIndex - 1)
        {
          bigIndex += (compare(data[bigIndex], data[(bigIndex + 1)], comp) < 0 ? 1 : 0);
        }

        if (compare(data[k], data[bigIndex], comp) >= 0) break;
        swap(data, k, bigIndex);
        k = bigIndex;
      }

    }

    return data;
  }

  private static <T> T[] quickSort(T[] data, int start, int end, Comparator<T> comp) {
    if (start < end)
    {
      int i = start;
      int j = end + 1;

      while (i < j) {
        swap(data, i, j);
      }

      swap(data, start, j);
      quickSort(data, start, j - 1, comp);
      quickSort(data, j + 1, end, comp);
    }
    return data;
  }

  private static <T> void swap(T[] data, int i, int j) {
    Object temp = data[i];
    data[i] = data[j];
    data[j] = (T) temp;
  }

  private static <T extends Comparable<T>> T[] getOrderSort(T[] data, OrderType type) {
    return (T[])getOrderSort(data, type, null);
  }

  private static <T> T[] getOrderSort(T[] data, OrderType type, Comparator<T> comp) {
    if ((type == OrderType.binaryInsert_asce) || (type == OrderType.binaryInsert_desc))
      binaryInsertSort(data, comp);
    else if ((type == OrderType.bubble_asce) || (type == OrderType.bubble_desc))
      bubbleSort(data, comp);
    else if ((type == OrderType.heap_asce) || (type == OrderType.heap_desc))
      heapSort(data, comp);
    else if ((type == OrderType.quick_asce) || (type == OrderType.quick_desc))
      quickSort(data, comp);
    else if ((type == OrderType.insert_asce) || (type == OrderType.insert_desc))
      insertSort(data, comp);
    else if ((type == OrderType.select_asce) || (type == OrderType.select_desc))
      selectSort(data, comp);
    else if ((type == OrderType.shell_asce) || (type == OrderType.shell_desc))
      shellSort(data, comp);
    else {
      binaryInsertSort(data, comp);
    }
    if (type.getKey().endsWith(OrderType.desc.getKey())) {
      ArrayUtils.reverse(data);
    }
    return data;
  }
  private static <T> int compare(T comp1, T comp2, Comparator<T> comparator) {
      if (comparator == null) {
          Comparable comp = (Comparable)comp1;
          return comp.compareTo(comp2);
      }
      return comparator.compare(comp1, comp2);
  }
  
 

  public static enum OrderType
  {
    desc("desc", "降序"), asce("asce", "升序"), 
    shell_desc("shell_desc", "插入排序->Shell排序->稳定"), 
    insert_desc("insert_desc", "插入排序->直接插入排序->稳定"), 
    quick_desc("quick_desc", "交换排序->快速排序->不稳定"), 
    bubble_desc("bubble_desc", "交换排序->冒泡排序->稳定"), 
    heap_desc("heap_desc", "选择排序 ->堆排序->不稳定"), 
    select_desc("select_desc", "选择排序 ->直接选择排序->已改进->不稳定"), 
    binaryInsert_desc("binaryInsert_desc", "插入排序->折半插入排序->稳定"), 
    shell_asce("shell_asce", "插入排序->Shell排序->稳定"), 
    insert_asce("insert_asce", "插入排序->直接插入排序->稳定"), 
    quick_asce("quick_asce", "交换排序->快速排序->不稳定"), 
    bubble_asce("bubble_asce", "交换排序->冒泡排序->稳定"), 
    heap_asce("heap_asce", "选择排序 ->堆排序->不稳定"), 
    select_asce("select_asce", "选择排序 ->直接选择排序->已改进->不稳定"), 
    binaryInsert_asce("binaryInsert_asce", "插入排序->折半插入排序->稳定");

    private String key;
    private String comment;

    private OrderType(String key, String comment) { this.key = key;
      this.comment = comment; }

    public String getKey()
    {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getComment() {
      return this.comment;
    }

    public void setComment(String comment) {
      this.comment = comment;
    }
  }
}