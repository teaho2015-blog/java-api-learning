/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.outofmemory;

import java.util.ArrayList;
import java.util.List;

/**
 *  make  <code>java.lang.OutOfMemoryError: PermGen space<code/> happen
 *
 *  VM options: -XX:MaxPermSize=10M
 */
public class PermGenSpaceMain {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 200000000; i++) {
            String s = String.valueOf(i);
            list.add(s.intern());
        }

    }
}
/* //java6 output
Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
	at java.lang.String.intern(Native Method)
	at com.tea.outofmemory.PermGenSpaceMain.main(PermGenSpaceMain.java:21)
 */

