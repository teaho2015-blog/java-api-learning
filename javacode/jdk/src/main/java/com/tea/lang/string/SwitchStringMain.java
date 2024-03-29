/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.lang.string;

public class SwitchStringMain {
    public static void main(String[] args) {
        String s = "abc";
        switch (s) {
            case "a":
                System.out.println("a");
                break;
            case "abc":
                System.out.println("abc");
                break;
            default:
                System.out.println("default");
                break;
        }
    }
}
