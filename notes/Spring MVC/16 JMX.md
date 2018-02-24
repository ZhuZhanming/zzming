#### Java管理扩展(Java Manage-ment Extensions)
* 核心组件MBean(managed bean)
>* 标准MBean bean实现这个接口
>* 动态MBean 在运行时通过调用DynamicMBean接口的方法确定
>* 开发MBean 特殊动态Bean,限定原始类型,原始类型的包装类和可以分解成以上类的任意类
>* 模型MBean 特殊动态Bean,用来充当管理接口与受管理资源的中介
```java
@Bean
public MBeanExporter mBeanExporter(SpringEmailApplication application) {
    MBeanExporter exporter = new MBeanExporter();
    Map<String, Object> beans = new HashMap<>();
    beans.put("cotroller:name=SpringEmailApplication", application);
    exporter.setBeans(beans);
    exporter.setAssembler(assembler());
    return exporter;
}
```
1. 通过名称暴露方法

```java
@Bean
public MethodNameBasedMBeanInfoAssembler assembler() {
    MethodNameBasedMBeanInfoAssembler assembler = new MethodNameBasedMBeanInfoAssembler();
    assembler.setManagedMethods("getAge","setAge");
    return assembler;
}
```
* 相反MethodExclusionMBeanInfoAssembler是反操作,setIgnoredMethods(...)
2. 使用接口定义MBean的操作和属性
```java
//首先定义接口
public interface ...ManagedOperations{
    int getAge();
    void setAge(int age);
}
//配置
@Bean
public InterfaceBasedMBeanInfoAssembler assembler() {
    InterfaceBasedMBeanInfoAssembler assembler = new InterfaceBasedMBeanInfoAssembler();
    assembler.setManagedInterfaces(ManagedOperations.class);
    return assembler;
    }
```
3. 使用注解驱动的MBean
```
//类上
@ManagedResource(objectName = "cotroller:name=SpringEmailApplication")
//方法上
@ManagedAttribute 或 @ManagedOperation
//暴露属性   和暴露方法
```
4. 处理冲突
```java
//FAIL_ON_EXISTING
//IGNORE_EXISTING
//REPLACING_EXISTING
exporter.setRegistrationPolicy(RegistrationPolicy....)
```
---
#### 远程MBean
1. 暴露远程MBean
```java
@Bean
public ConnectorServerFactoryBean connectorServerFactoryBean() {
    return new ConnectorServerFactoryBean();
}
```
* 默认绑定到:service:jmx:jmxmp://localhost:9875
* 可以修改为rmi
```java
@Bean
public ConnectorServerFactoryBean connectorServerFactoryBean() {
    csfb = new ConnectorServerFactoryBean()
    csfb.setServiceUrl("service:jmx:rmi://localhost/jndi/rmi://localhost:1099/person");
    return new ConnectorServerFactoryBean();
}
//还需要一个RMI注册表,写在前面
@Bean
public RmiRegistryFactoryBean rmiRegistryFB() {
    RmiRegistryFactoryBean registryFB = new RmiRegistryFactoryBean();
    registryFB.setPort(1099);
    return registryFB;
}
```
2. 访问远程MBean
```java
@Bean
public MBeanServerConnectionFactoryBean fb() throws MalformedURLException {
    MBeanServerConnectionFactoryBean fb = new MBeanServerConnectionFactoryBean();
    fb.setServiceUrl("service:jmx:rmi://localhost/jndi/rmi://localhost:1099/person");
    return fb;
}
//使用
@Autowired
private MBeanServerConnection connection;
System.out.println(connection.getMBeanCount());
connection.queryMBeans(null, null);
System.out.println(connection.getAttribute(new ObjectName("person:name=zzming"), "age"));
connection.setAttribute(new ObjectName("person:name=zzming"), new Attribute("age", 25));
```
~~失败,bean注入冲突~~

2.1 代理MBean
```java
@Bean
public MBeanProxyFactoryBean fb(MBeanServerConnection connection) throws MalformedObjectNameException {
    MBeanProxyFactoryBean fb = new MBeanProxyFactoryBean();
    fb.setObjectName("person:name=zzming");
    fb.setServer(connection);
    //MBean实现的接口
    fb.setProxyInterface(Runnable.class);
    return fb;
}
```
---
3. 处理通知

3.1 放送通知
```java
//实现NotificationPublisherAware
//@ManagedNotification(notificationTypes = "type", name = "TODO")
@Override
public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
    notificationPublisher.sendNotification(new Notification("type", this, 0));
}
```
3.2 监听通知
```java
//实现NotificationListener
//用MBeanExporter注册他
exporter.setNotificaionListenerMappings();
```