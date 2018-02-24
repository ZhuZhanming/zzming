#### 参数
>* -Dfile.encoding=UTF-8
>* -XX:+HeapDumpOnOutOfMemoryError
### 初体验
* 参数
>* -Xms20m -Xmx20m  限制Java堆大小为20MB，不可扩展
>* -XX:HeapDumpPath=E:\job 指定路径
>* -XX:SurvivorRatio=8 存活比2:8
#### 工具
* Eclipse Memory Analyzer
>* 在Eclipse Marketplace 搜索memory
#### java技术体系
>* Java程序设计语言
>* 各硬件平台上的Java虚拟机
>* Class文件格式
>* Java API
>* 第三方的Java类库
#### jvm 的发展
* 1.0 classic vm
>* 第一款商用java虚拟机
>* 只能使用纯解释器的方式来执行java代码
* 2.0 Exact VM
>* 编译器和解释器混合工作以及两级即时编译器
>* 只在Solaris平台发布
>* 英雄气短
* 3.0 hotspot
>* 称霸武林
>* ....
* KVM
>* 简单，轻量，高度可移植
>* 在手机平台运行
* JRockit
>* 世界上最快的Java虚拟机
>* 专注于服务端应用
>* 带生产环境内存泄漏检查工具
* Dalvik
>* Android 
>* 不符合jvm规范
>* 执行Dex dalvik Executable
* Microsoft JVM
>* 被sun告了
* Azul VM Liquid VM
>* 高性能,但专用
>* 本身就是一个操作系统
* TaobaoVM
>* 淘宝自己定制的
>* 只能使用Inter的CPU
#### Java虚拟机内存管理
* 线程共享区
>* 方法区
>* java堆(堆内存)
* 线程独占区
>* 虚拟机栈(栈内存)，本地方法栈，线程计数器.(Hotsppt将两个栈和在了一起。)
##### Java虚拟机栈
>* 定义:java方法执行的动态内存模型
>* 栈帧:每个方法执行都会创建一个栈帧,伴随着方法从创建到执行完成。用于存储局部变量表，操作数栈，动态连接，方法出口等。
>* 局部变量表：存放编译器可知的各种基本数据类型，引用类型，returnAddress类型。内存空间在编译器完成分配，当进入一个方法时，这个方法需要在帧分配多少内存时固定的
>* 递归调用error:StackOverflowError;OutOfMemoryError
##### 本地方法栈
>* 本地方法栈是为虚拟机执行native方法服务。虚拟机栈为虚拟机执行Java方法服务。
##### Java堆
>* 存放对象实例(性能优化:不一定)
>* 垃圾收集器管理的主要区域
>* 新生代，老年代，Eden空间
##### 方法区
>* 虚拟机加载的类信息(类的版本，字段，方法，接口)，常量，静态变量，即时编译器后的代码等数据
>* Hotspot用永久代实现了方法区
>* 垃圾回收在方法区的行为
>* 异常的定义
##### 运行时常量池-方法区的一部分

























