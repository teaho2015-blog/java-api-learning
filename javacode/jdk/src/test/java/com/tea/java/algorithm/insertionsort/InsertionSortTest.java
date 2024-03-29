package com.tea.java.algorithm.insertionsort;

import com.tea.java.algorithm.mergesort.MergeSort2;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author teaho2015@gmail.com
 * @date 2021-07
 */
public class InsertionSortTest {



    @Test
    public void testInsertionSort() {
        Integer[] arr = new Integer[]{ 12, 6, 135, 9, 10, 15, 2, 22};
        Integer[] target = Arrays.copyOf(arr, arr.length);
        Arrays.sort(target, Integer::compareTo);
        new InsertionSort().sort(arr);
        Assert.assertTrue(Arrays.deepEquals(arr, target));

    }
}
