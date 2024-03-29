/**
 * @author teaho2015@gmail.com
 * @since 2017
 */
package com.tea.lang.classloader;

/**
 * get some classloaders
 */
public class ShowClassLoadersMain {


    public static void main(String[] arg) {

        ClassLoader c = ShowClassLoadersMain.class.getClassLoader();  //获取Test类的类加载器

        System.out.println(c);

        ClassLoader c1 = c.getParent();  //获取c这个类加载器的父类加载器

        System.out.println(c1);

        ClassLoader c2 = c1.getParent();//获取c1这个类加载器的父类加载器

        System.out.println(c2);

    }

}
