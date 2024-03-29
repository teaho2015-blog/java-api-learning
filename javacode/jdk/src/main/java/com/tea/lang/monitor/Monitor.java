/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.lang.monitor;

public class Monitor {
    public static void main(String[] args) {
        synchronized (Monitor.class) {
            String test = "a";
        }
    }
}
