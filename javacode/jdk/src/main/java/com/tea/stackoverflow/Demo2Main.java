/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.stackoverflow;

public class Demo2Main {
    public static void main(String[] args) {

        Demo2Main dm = new Demo2Main();
        System.out.println(dm);
    }

    private String id = "abc";

    @Override
    public String toString() {
        return "toString Method " + this + ", id=" + id;
    }

}
