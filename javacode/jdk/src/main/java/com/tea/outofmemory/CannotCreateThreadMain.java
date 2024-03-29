/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.outofmemory;

import java.util.concurrent.TimeUnit;

/**
 * make  <code>java.lang.OutOfMemoryError: Unable to create new native thread<code/> happen
 *
 * can not work at windows
 *
 *  VM options:
 *
 */
public class CannotCreateThreadMain {
    public static void main(String[] args) {

        for (int i = 0; ; i++) {
            new Thread(() -> {
                try {
                    TimeUnit.MINUTES.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i))
                    .start();
        }
    }
}
