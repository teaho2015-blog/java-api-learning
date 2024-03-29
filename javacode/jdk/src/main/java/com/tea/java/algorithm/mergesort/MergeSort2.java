package com.tea.java.algorithm.mergesort;

/**
 * <p>归并排序
 *
 * <p>自底向上的方式
 *
 *  <p>时间复杂度: O(1/2NlgN)到O(NlgN)
 *  <p>空间复杂度: O(N)
 *
 * @author teaho2015@gmail.com
 */
public class MergeSort2 {


    public void sort(Comparable[] arr) {

        //进行lgN此两两归并
        int len = arr.length;
        Comparable[] temp = new Comparable[arr.length];
        //sz为划分子数组的长度
        for (int sz = 1; sz < len; sz += sz) {
            for (int i = 0; i < len - sz; i += (sz + sz)) {
                merge(arr, i, i + sz - 1, Math.min(i + sz + sz - 1, len - 1), temp);
            }
        }


    }

    public void merge(Comparable[] arr, int lf, int mid, int rt, Comparable[] temp) {

        int i = lf, j = mid + 1;
        for (int k = lf; k <= rt; k++) {
            temp[k] = arr[k];
        }

        for (int k = lf; k <= rt; k++) {
            if (i > mid) {
                arr[k] = temp[j++];
            } else if(j > rt) {
                arr[k] = temp[i++];
            } else if (temp[i].compareTo(temp[j]) < 0) {
                arr[k] = temp[i++];
            } else {
                arr[k] = temp[j++];
            }
        }

    }
}
