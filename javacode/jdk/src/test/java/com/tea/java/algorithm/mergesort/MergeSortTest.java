package com.tea.java.algorithm.mergesort;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author teaho2015@gmail.com
 * @date 2021-07
 */
public class MergeSortTest {


    @Test
    public void testMergeSort1() {

        Integer[] arr = new Integer[]{ 12,6, 135, 9, 10, 15, 2, 22};
        new MergeSort1().sort(arr);
        System.out.println(Arrays.toString(arr));

    }


    @Test
    public void testMergeSort2() {
        Integer[] arr = new Integer[]{ 12,6, 135, 9, 10, 15, 2, 22};
        new MergeSort2().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

}
