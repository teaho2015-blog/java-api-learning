# 插入排序

插入排序（英语：Insertion Sort）是一种简单直观的排序算法。
它的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。
插入排序在实现上，通常采用in-place排序（即只需用到O(1)的额外空间的排序），
因而在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。

![Insertion-sort-example-300px.gif](Insertion-sort-example-300px.gif)

--摘自Wikipedia

## 场景

优点：

* 实现简单
* 稳定的
* 对于小量数据排序来说是有优势的
* 不需要使用额外空间

缺点：
* 对于稍微大的数据集的排序执行是缓慢的


**由于以上特点**，
在市面上，我们可以看到在C/C++的STL的sort算法和stdlib的qsort算法中，他们将插入排序作为快速排序的补充，用于少量元素的排序（通常为8个或以下）。
而在Java中，我们可以看到DualPivotQuicksort中，对小于47个元素的数组会使用插入排序算法进行排序。


## 自实现

[include:2-](../../javacode/jdk/src/main/java/com/tea/java/algorithm/insertionsort/InsertionSort.java)


## references

[1][插入排序|wikipedia](https://zh.wikipedia.org/wiki/%E6%8F%92%E5%85%A5%E6%8E%92%E5%BA%8F)