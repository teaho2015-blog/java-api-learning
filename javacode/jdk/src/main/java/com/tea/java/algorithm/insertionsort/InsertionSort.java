package com.tea.java.algorithm.insertionsort;

/**
 * <p>插入排序
 *
 * <p>时间复杂度：O(N)~O(N^2)
 * <p>空间复杂度：O(1)
 *
 * @author teaho2015@gmail.com
 * @date 2021-07
 */
public class InsertionSort {

    public void sort(Comparable[] arr) {

        int len = arr.length;
        for (int i = 0, j = i; i < len - 1; j = ++i) {
            Comparable cursor = arr[i + 1];
            while (cursor.compareTo(arr[j]) < 0) {
                arr[j + 1] = arr[j];
                if (j-- == 0) {
                    break;
                }
            }
            arr[j + 1] = cursor;
        }
    }


}
