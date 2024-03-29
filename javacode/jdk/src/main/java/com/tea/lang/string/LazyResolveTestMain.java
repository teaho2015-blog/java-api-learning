/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.lang.string;

public class LazyResolveTestMain {
    public static void main(String[] args) {
        test('h', 'e', 'l', 'l', 'o');
        test('m', 'a', 'i', 'n');
    }
    static void test(char... arg) {
        String s1 = new String(arg), s2 = s1.intern();
        System.out.println('"'+s1+'"'
                +(s1!=s2? " existed": " did not exist")+" in the pool before");
        System.out.println("is the same as \"hello\": "+(s2=="hello"));
        System.out.println("is the same as \"main\": "+(s2=="main"));
        System.out.println();
    }
}
//output
/* //java7 and above output
"hello" did not exist in the pool before
is the same as "hello": true
is the same as "main": false

"main" existed in the pool before
is the same as "hello": false
is the same as "main": true
 */