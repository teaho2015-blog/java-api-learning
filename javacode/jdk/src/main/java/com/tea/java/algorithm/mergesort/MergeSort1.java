package com.tea.java.algorithm.mergesort;

/**
 *  <p>归并排序
 *
 *  <p>自顶向下
 *
 *  <p>时间复杂度: O(1/2NlgN)到O(NlgN)
 *  <p>空间复杂度: O(N)
 *
 * @author teaho2015@gmail.com
 */
public class MergeSort1 {

    public void sort(Comparable[] arr) {
        Comparable[] temp = new Comparable[arr.length];

        sort(arr, 0, arr.length - 1, temp);
    }

    public void sort(Comparable[] arr, int lf, int rt, Comparable[] temp) {

        if (lf >= rt) {
            return;
        }
        int mid = (lf + rt) / 2;
        //左边
        sort(arr, lf, mid, temp);
        //右边
        sort(arr, mid + 1, rt, temp);
        merge(arr, lf, mid, rt, temp);
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
