/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.lang.string;

public class StringInternTestMain {
    public static void main(String[] args) {
        test('h', 'e', 'l', 'l', 'o');
    }
    static void test(char... arg) {
        String s1 = new String(arg), s2 = s1.intern();
        System.out.println(s1==s2);
        System.out.println();
    }
}
//output
/* //java6
false
 */

/* //java7 and above output
true
 */