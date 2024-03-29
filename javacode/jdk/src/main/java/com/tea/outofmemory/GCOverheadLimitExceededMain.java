/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.outofmemory;

import java.util.*;

/**
 * make  <code>java.lang.OutOfMemoryError: GC Overhead limit exceeded<code/> happen
 *
 *  VM options: -Xmx10m
 *              -Xms10m
 *              -XX:-OmitStackTraceInFastThrow
 *              -verbose:gc
 *              -XX:+PrintGCDetails
 *              -XX:+PrintGCTimeStamps
 *
 */
public class GCOverheadLimitExceededMain {
    public static void main(String[] args) {
        int _1MB = 1024 * 1024;

        Map<Integer, Integer> m = new HashMap<>();
        Random r = new Random();
        for (int i = 1; ; i++) {
            m.put(r.nextInt(), i);
        }

    }
}
/* //Java8 output
//……省略
31.504: [Full GC (Ergonomics) [PSYoungGen: 2048K->2048K(2560K)] [ParOldGen: 7041K->7041K(7168K)] 9089K->9089K(9728K), [Metaspace: 3233K->3233K(1056768K)], 0.1488742 secs] [Times: user=0.53 sys=0.00, real=0.15 secs]
31.653: [Full GC (Ergonomics) [PSYoungGen: 2048K->2048K(2560K)] [ParOldGen: 7043K->7043K(7168K)] 9091K->9091K(9728K), [Metaspace: 3233K->3233K(1056768K)], 0.1370669 secs] [Times: user=0.56 sys=0.00, real=0.14 secs]
31.793: [Full GC (Ergonomics) [PSYoungGen: 2048K->0K(2560K)] [ParOldGen: 7046K->709K(7168K)] 9094K->709K(9728K), [Metaspace: 3258K->3258K(1056768K)], 0.0157233 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
	at java.lang.Integer.valueOf(Integer.java:832)
	at com.tea.outofmemory.GCOverheadLimitExceededMain.main(GCOverheadLimitExceededMain.java:30)

Heap
 PSYoungGen      total 2560K, used 53K [0x00000000ffd00000, 0x0000000100000000, 0x0000000100000000)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
  eden space 2048K, 2% used [0x00000000ffd00000,0x00000000ffd0d7a8,0x00000000fff00000)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
  from space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
	at java.lang.reflect.Method.invoke(Method.java:498)
  to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:140)
 ParOldGen       total 7168K, used 709K [0x00000000ff600000, 0x00000000ffd00000, 0x00000000ffd00000)
  object space 7168K, 9% used [0x00000000ff600000,0x00000000ff6b1738,0x00000000ffd00000)
 Metaspace       used 3265K, capacity 4494K, committed 4864K, reserved 1056768K
  class space    used 356K, capacity 386K, committed 512K, reserved 1048576K

 */
