/**
 * @author teaho2015@gmail.com
 */
package com.tea.lang.class0;

public class NewInstanceMain extends NewInstanceParent {

    public static A as1 = new A(3);

    public A a1 = new A(11);

    static {
        System.out.println(4);
    }

    public NewInstanceMain() {
        System.out.println(13);
    }

    public static void main(String[] args) {
        System.out.println(7);
        NewInstanceMain newInstanceMain = new NewInstanceMain();
    }

    public static A as2 = new A(5);

    public A a2 = new A(12);

    static {
        System.out.println(6);
    }
}
