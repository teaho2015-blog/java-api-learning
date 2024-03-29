/**
 * @author teaho2015@gmail.com
 * since 2017
 */
package com.tea.lang.string;

import java.lang.reflect.Field;

public class ReflectStringMain {

    public static void main(String[] args) {
        String origin = "abc";
//        String test = origin.intern();
        String test = new String(origin.getBytes());
        Class strClass = test.getClass();
        try {
            Field field = strClass.getDeclaredField("value");
            field.setAccessible(true);
            char c[] = (char[])field.get(test);
            for (char cs : c) {
                System.out.println(cs);
            }
            char d = 'd';
            c[0] = d;
            System.out.println(test);
            System.out.println(test.intern());
            System.out.println(origin);
            System.out.println(origin.intern());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
