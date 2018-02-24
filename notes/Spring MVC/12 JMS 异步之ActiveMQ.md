### 消息代理(message broker)和目的地(destination)
客户端将消息放送给消息代理,消息代理确保消息能送给目的地
* 队列(queue):点对点
* 主题(topic):发布和订阅
#### 点对点模型
可以有多个消费者同时消费
#### 主题
各个消费者的动作可以不一样
### 消息代理
* ActiveMQ 和HornetQ
### ActiveMQ
1. 导包
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```
2. 注入springboot自动配置的JmsOperations(JmsTemplate)
3. jms.send("zzming",s -> s.createTextMessage(str));
>* 若目的地不存在,就创建一个队列形式的目的地
>* 存在 :springbean:org.apache.activemq.command.ActiveMQTopic(Queue)
>* spring.jms.template.default-destination---不用指定信息目的地了
4. 或在放送前转换信息
>* MappingJackson2
>* Simple String与TextMessage 字节数组与ByteMessage Map与MapMessage Serializable 与ObjectMessage
5. 接收信息 jms.receive();但是同步的会阻塞
```java
public String receive() {
    try {
        return ((ActiveMQTextMessage )jms.receive("zzming")).getText();
    } catch (JMSException e) {
        throw JmsUtils.convertJmsAccessException(e);
    }
}
//或者
jms.convertAndSend(str);
return jms.receiveAndConvert();
//或者在Spring bean的方法上
@JmsListener(destination = "zzming")
```
### 使用基于消息的RPC  ~~失败~~
1. 客户端
```java
@Bean
public JmsInvokerServiceExporter exporter(PersonService personService) {
    JmsInvokerServiceExporter exporter = new JmsInvokerServiceExporter();
    exporter.setService(personService);
    exporter.setServiceInterface(PersonService.class);
    return exporter;
}
```
2. 服务端
```java
@Bean
public JmsInvokerProxyFactoryBean proxy(ConnectionFactory factory) {
    JmsInvokerProxyFactoryBean jms = new JmsInvokerProxyFactoryBean();
    jms.setConnectionFactory(factory);
    jms.setQueueName("zzming");
    jms.setServiceInterface(PersonService.class);
    return jms;
}
```