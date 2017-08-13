# java.lang.StackOverflowError

## 什么是StackOverflowError

StackOverflowError是一个VirtualMachineError，Error类及其子孙描述了Java运行时系统的内部错误和资源耗尽错误。

当虚拟栈或本地栈需要比已配置的更多内存时，就会抛出java.lang.StackOverflowError。

要明白什么是虚拟栈和本地栈，可参考[JVM的内存结构](../java.lang/java-memory-structure.html)。


## 设置栈大小

-Xss和-XX:ThreadStackSize=size是等价的设置栈大小的两个Java参数。

默认值视乎虚拟内存大小。

例子：
-Xss1g
-Xss1024m
-Xss1048576k
-Xss1073741824

设置该参数（栈大小）的时候，要明白一点，一般内存资源是一定的有限的，当我们增大该参数的同时也意味着最大线程数的减少。


## debug

一般发生java.lang.StackOverflowError是出现了错误的递归调用。我认为，结合代码，一般都比较容易发现。

说两个有点隐蔽的例子：

#### 第一个例子

参考自[《thinking in Java》<sup>[2]<sup>](#references)。

[include:7-](../../javacode/src/main/java/com/tea/stackoverflow/Demo2Main.java)

运行代码会报如下错误：

~~~

Exception in thread "main" java.lang.StackOverflowError
	at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:422)
	at java.lang.StringBuilder.append(StringBuilder.java:136)
	at com.tea.stackoverflow.Demo2Main.toString(Demo2Main.java:18)
	at java.lang.String.valueOf(String.java:2994)
	at java.lang.StringBuilder.append(StringBuilder.java:131)
	at com.tea.stackoverflow.Demo2Main.toString(Demo2Main.java:18)
	at java.lang.String.valueOf(String.java:2994)
	at java.lang.StringBuilder.append(StringBuilder.java:131)
	at com.tea.stackoverflow.Demo2Main.toString(Demo2Main.java:18)
	at java.lang.String.valueOf(String.java:2994)

	//省略n行

~~~

原因可自行思考或看书。

#### 第二个例子

参考自[stackoverflow | What is a StackOverflowError?][link: 1]。

有时，可能我们的代码正调用系统或VM的功能，而系统反过来调用我们的代码，产生间接递归。如，链接中举的关于页面渲染的例子。



## references

[1][stackoverflow | What is a StackOverflowError?][link: 1]

[2]Bruce Eckel.java编程思想,第4版.中国:机械工业出版社,2007


[link: 1]: https://stackoverflow.com/questions/214741/what-is-a-stackoverflowerror





