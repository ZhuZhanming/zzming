#### JVM的结构
* 类加载器
* 执行引擎
* 运行时数据区
* 本地接口
Class Files->Classloader->运行时数据区->执行引擎，本地接口->本地方法库
* 类的加载
>* 加载，连接(验证，准备，解析),初始化，使用，卸载
>* Class 保存类的定义或结构， 堆中
* 初始化:执行类的构造器<clinit>,为类的静态变量赋值
* 构造器:

* JDK已有的类加载器
>* BootStrap Classloader   >rt.jar
>* Extension Classloader  ->lib/ext/*.jar
>* App Classloader都继承ClassLoader->classpath
>* 自定义类加载器
#### 双亲委派模型
* 调用parent.loadClass
* 自定义类加载器
>* 继承 ClassLoader
>* 重写findClass