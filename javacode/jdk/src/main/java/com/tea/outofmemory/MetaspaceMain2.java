/**
 *
 */
package com.tea.outofmemory;

/**
 * copy from https://plumbr.eu/outofmemoryerror
 *
 * test java.lang.OutOfMemoryError: Metaspace
 *
 * *  VM options: -XX:MaxMetaspaceSize=10M
 *              -verbose:gc
 *              -XX:+PrintGCDetails
 *              -XX:+PrintGCTimeStamps
 */
public class MetaspaceMain2 {
    static javassist.ClassPool cp = javassist.ClassPool.getDefault();


    public static void main(String[] args) throws Exception{
        for (int i = 0; ; i++) {
            Class c = cp.makeClass("com.tea.outofmemory.metaspace" + i).toClass();
        }
    }

}


/*//java8 output
Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
	at javassist.ClassPool.toClass(ClassPool.java:1085)
	at javassist.ClassPool.toClass(ClassPool.java:1028)
	at javassist.ClassPool.toClass(ClassPool.java:986)
	at javassist.CtClass.toClass(CtClass.java:1079)
	at com.tea.outofmemory.MetaSpaceMain2.main(MetaSpaceMain2.java:16)
 */