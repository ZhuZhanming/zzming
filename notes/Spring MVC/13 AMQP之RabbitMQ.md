* 优势
>* 定义了线路层(wire-level protocol)协议
>* JMS是API规范
>* JMS确保所有实现可以通过通用API来使用,不能确保能被其他JMS实现所用
>* AMQP规范了消息的格式
>* 综上:AMQP能跨不同的AMQP实现,语言和平台
* 生产者将消息放送给==Exchange==,Exchange==路由==到队列,消费者从队列中取数据,一下==4==种Exchange
* 如果消息的routing key与binding的routing key
>* Direct 直接匹配
>* Topic 符合通配符匹配
>* Headers 消息参数表中的头信息和值与bingding参数表中相匹配
>* Fanout 都路由
#### 使用
1. 导包
```java
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```
>* 默认localhost:5672
>* 管理员localhost:15672   guest/guest

2. 配置
```properties
spring.rabbitmq.publisher-confirms=true   #开启回调
```
3. 注入bean  Direct只需要注入Queue
```java
//1注入队列
@Bean
public Queue queue() {
    return new Queue("person.queue", false);
}
//2注入Exchange 有Direct,Topic,Headers,Fanout
@Bean
public TopicExchange topic() {
    return new TopicExchange("topic");
}
//3注入Binding #多个字符  %1个字符
@Bean Binding binding(Queue queue,TopicExchange topic) {
    return BindingBuilder.bind(queue).to(topic).with("person.#");
}
```
4. 使用
4.1 Direct
```java
//生产者
rabbitTemplate.convertAndSend("helloQueue", sendMsg);
//消费者
@RabbitListener(queues = "helloQueue")
@RabbitHandler
```
4.2 Topic  三个参数都要传
```
rabbitTemplate.convertAndSend("exchange", "topic.message", msg1);
//只要第二个参数满足通配符,就能接收到:routing_key
```
4.3 Fanout
```java
//第二个参数随便写
//有几个Queue就要绑定几次到fanoutExchange上
rabbitTemplate.convertAndSend("fanoutExchange","abcd.ee", msgString);
```
5. 使用回滚
```
//implements  RabbitTemplate.ConfirmCallback
rabbitTemplatenew.convertAndSend("exchange", "topic.messages", msg, correlationData)
rabbitTemplatenew.setConfirmCallback(this);
```