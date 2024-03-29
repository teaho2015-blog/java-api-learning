/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.outofmemory;

/**
 *  make  <code>java.lang.OutOfMemoryError: Requested array size exceeds VM limit<code/> happen
 *
 *  VM options: -Xmx10m
 */
public class RequestedArraySizeExceedsVMLimitMain {

    public static void main(String[] args) {
        int[] i = new int[Integer.MAX_VALUE];

    }
}

/*//java8 output
Exception in thread "main" java.lang.OutOfMemoryError: Requested array size exceeds VM limit
	at com.tea.outofmemory.RequestedArraySizeExceedsVMLimitMain.main(RequestedArraySizeExceedsVMLimitMain.java:12)
    (.....省略)
*/
