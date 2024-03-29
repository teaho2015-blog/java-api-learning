/**
 * @author teaho2015@gmail.com
 */
package com.tea.lang.class0;

public class NewInstanceParent {
    public static A as1 = new A(1);

    public A a1 = new A(8);
    static {
        System.out.println(2);
    }

    public NewInstanceParent() {
        System.out.println(10);
    }

    public A a2 = new A(9);
}
