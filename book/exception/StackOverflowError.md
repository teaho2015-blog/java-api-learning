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

一般发生java.lang.StackOverflowError是出现了错误的递归调用。
又或者，相对较小出现的错误是，调用层次过于冗杂。
以上我认为，结合代码，一般都比较容易发现。

说两个有点隐蔽的例子：

#### 第一个例子

参考自[《thinking in Java》<sup>[2]<sup>](#references)。

[include:7-](../../javacode/jdk/src/main/java/com/tea/stackoverflow/Demo2Main.java)

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


## JVM Crash的stack overflow

一个延伸的问题。

以下摘自[Java trouble shooting guide | 5.1.5 Crash Due to Stack Overflow](http://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/crashes001.html#CIHDGFJA)

>A stack overflow in Java language code will normally result in the offending thread throwing the java.lang.StackOverflowError exception. On the other hand, C and C++ write past the end of the stack and provoke a stack overflow. This is a fatal error which causes the process to terminate.
>
>In the HotSpot implementation, Java methods share stack frames with C/C++ native code, namely user native code and the virtual machine itself.

意思是，Java使用的JVM实现Hotspot VM中，Java的代码和本地C或C++代码公用相同的栈帧（stack frame）。Java代码的错误会引起Java
一般Java代码的 stack overflow会抛出java.lang.StackOverflowError，而本地C或C++代码stack overflow的话会引起Crash。

而如何debug这类stacck overflow呢？有如下官方文档参考：

JVM crash会生成Fatal Error Log文件，位置在哪可参考链接。[Troubleshooting Guide | Location of Fatal Error Log](http://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/felog001.html#BABIDHJC)

错误Log的头部信息[Troubleshooting Guide | Header Format](http://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/felog003.html#BABFFJBB)

[Troubleshooting Guide | 5.1.5 Crash Due to Stack Overflow](http://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/crashes001.html#CIHDGFJA)

这篇文章提供了一些全面的深入底层的分析和解决方案。[简书 | 作者：你假笨 | JVM源码分析之栈溢出完全解读](http://www.jianshu.com/p/debef4f69a90)

Stack Overflow上的一个回答，介绍了`-XX:StackYellowPages=size`、`-XX:StackRedPages=size`、`-XX:StackRedPages=size`和一些相关知识，供需设置这三个参数的参考。[Stack Overflow | What is thread stack size option(-Xss) given to jvm? Why does it have a limit of atleast 68k in a windows pc?](https://stackoverflow.com/a/25318740/6874711)
**未解决疑惑**：至今未找到答案和HotSpot的相关文档说明，为什么这三个参数没出现在官方的参数文档中[Java Tools Reference | java](http://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html)。

## references

[1][stackoverflow | What is a StackOverflowError?][link: 1]

[2]Bruce Eckel.java编程思想,第4版.中国:机械工业出版社,2007


[link: 1]: https://stackoverflow.com/questions/214741/what-is-a-stackoverflowerror






