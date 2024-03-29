/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.lang.string;

public class FinalStringMain {
    static void isFinal() {
        final String s = "abc";
        String test = s + "def";
        String a = "abcdef";
        System.out.println(test == a);
    }

    static void notFinal() {
        String s = "opq";
        String test = s + "rst";
        String a = "opqrst";
        System.out.println(test == a);
    }

    public static void main(String[] args) {
        isFinal();
        notFinal();
    }
}
