# java.lang.OutOfMemoryError

## 什么是OutOfMemoryError

当JVM由于内存不足不能够分配一个对象空间，并且通过垃圾回收也不能获取到更多的内存时，
将抛出OutOfMemoryError。

## 设置堆大小

计算对象大小。


## 垃圾回收

[垃圾回收(garbage collection, GC)](../language/gc.html)与OOM有千丝万缕的关系。由于gc不能够释放更多的空间，所以才爆OOM。
而且，理解GC，了解对象什么条件下会进入年老代，full GC又是什么条件下触发，GC对于软引用、弱引用、幽灵引用的处理，等等，能够帮助解决OOM甚至很大程度在coding阶段避免写出一些可能会触发OOM的代码。
再者，据说GC本身还可能会引发OOM呢。


## heap dump

heap dump是反映堆内存的镜像。能反映heap使用度，对象的字节数、个数，等等信息。

### 如何获得heap dump

1. 命令行参数

`-XX:+HeapDumpOnOutOfMemoryError`可开启使JVM 爆outOfMemoryError时在运行目录产生hprof文件。
而`-XX:HeapDumpPath=path`可使自定义文件路径及文件名。然后通过[jhat](../tools/jhat.html)分析。这个方法在生产环境中挺实用的。

2. [jmap](../tools/jmap.html)，然后通过[jhat](../tools/jhat.html)

3. GUI工具

jvisualVM, JProfiler, MAT, IBM HeapAnalyzer等等。
这些Profiling工具会使用JVM的调试接口(debuging interface)来搜集对象的内存分配信息，包括具体的代码行和方法调用栈。

在分析没爆内存还在运行的程序，使用这种方式会得到更多信息，但是，对运行中的JVM会有开销。

## 如何分析OutOfMemoryError

首先出现java.lang.OutOfMemoryError，建议一般先增大你的[堆大小](#设置堆大小)。
因为，很多时候it works，甚至是在你以为是内存泄露的情况。如果OutOfMemoryError是由于内存泄露，就算增大了堆大小也同样会再次发生。
当然，如果真的超出了你本来估算的很多，那就直接分析吧。

分析的话，当然是结合heap dump、环境和gc log分析。

首先对照下面的[错误集](#错误集)帮助定位，有一些OutOfMemoryError是比较容易发现的，比如，方法区引起的爆内存。
再结合工具，在我使用过的工具中jhat是最简易直接的，而jvisualVM、JProfiler、MAT中，我个人偏好MAT，因为MAT会有额外的错误分析建议。
IBM HeapAnalyzer我暂时没使用过。

然后，重点关注存活对象中数量排名前几位的那几个，这些对象一般不是引发OutOfMemoryError的源头，但很可能会和源头相关。
那么，我们可以沿着引用链结合代码寻找和思考。

事实上，引发OutOfMemoryError的问题千奇百怪，其中一些绝非能轻易追踪到，
遗憾的是，以我的经验仅能总结如上，分析OutOfMemoryError并不能仅从一时的学习能完善，
我认为，这事儿需长久地从复杂的生产环境和自我学习总结中慢慢精进。

以下是从网上发现的比较好比较“老炮儿”的分析OutOfMemoryError的文章。

[ImportNew | 深入解析OutOfMemoryError](http://www.importnew.com/22173.html)

[Plumbr | java.lang.OutOfMemoryError The 8 symptoms that surface them](https://plumbr.eu/outofmemoryerror)

IBM HeapAnalyzer等工具的架构师和开发者所写。[Unveiling the java.lang.Out OfMemoryError](http://jinwoohwang.sys-con.com/node/1229281)

## 错误集

### Requested array size exceeds VM limit

[include:7-](../../javacode/src/main/java/com/tea/outofmemory/RequestedArraySizeExceedsVMLimitMain.java)

这是一个较少出现的错误。该错误信息表明，一个数组(动态或静态地)请求过大的内存空间，大到虚拟机不能接受。

不用多说，一般该错误看源码。

### PermGen space

以下代码基于java6。
[include:7-](../../javacode/src/main/java/com/tea/outofmemory/PermGenSpaceMain.java)

`java.lang.OutOfMemoryError:PermGen space`只出现于Java7或以下版本，一般通过`-XX:MaxPermSize=size`增大永久代去解决错误。

### Metaspace

[include:6-](../../javacode/src/main/java/com/tea/outofmemory/MetaspaceMain2.java)

`java.lang.OutOfMemoryError: Metaspace`。
Java8使用本地内存存放Metaspace。Metaspace存放了Java类的元数据：类的版本、字段、方法、接口等描述信息。

Metaspace究竟是啥么，与PermGen又有什么关系，Java6、7、8这3个版本中二者有什么爱恨情仇。对以上3个问题有疑问的好奇宝宝，可以看这两篇文章：

[ifeve | Java PermGen 去哪里了?](http://ifeve.com/java-permgen-removed/)

[javacodegeeks | Java 8: From PermGen to Metaspace](https://www.javacodegeeks.com/2013/02/java-8-from-permgen-to-metaspace.html)

###

## references

[1][Plumbr | java.lang.OutOfMemoryError - The 8 symptoms that surface them][link: 1]

[2][ImportNew | 深入解析OutOfMemoryError][link: 2]

[3][kdgregory.com | Java Reference Objects or How I Learned to Stop Worrying and Love OutOfMemoryError][link: 3]

[4][JINWOO HWANG | Unveiling the java.lang.Out OfMemoryError][link: 4]


[]Bruce Eckel.java编程思想,第4版.中国:机械工业出版社,2007


[link: 1]: https://plumbr.eu/outofmemoryerror

[link: 2]: http://www.importnew.com/22173.html

[link: 3]: http://www.kdgregory.com/index.php?page=java.refobj

[link: 4]: http://jinwoohwang.sys-con.com/node/1229281


