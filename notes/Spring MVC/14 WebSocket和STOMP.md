### Websocket
>* 套接字实现全双工通信
>* 异步通信
>* 全双工通信:浏览器和服务器双向通信

### 低层级WebSocketAPI
1. 继承 PersonHandler extends AbstractWebSocketHandler
>* TextWebSocketHandler子类  接收文本消息,拒绝二进制(关闭连接)
>* BinaryWebSocketHandler子类  与上相反
```java
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/person");
    }
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new PersonHandler();
    }
}
```
```js
var url = 'ws://'+window.location.host+'/websocket/person';
var sock = new WebSocket(url);   //开启WebSocket
sock.onopen = function(){        //处理连接开启事件
    console.log('Opening');
    sayPerson();
};
sock.onmessage = function(e){    //处理消息
    console.log('message:'+e.data);
    setTimeout(function(){sayPerson()},2000);
};
sock.onclose = function(){       //处理连接关闭事件
    console.log("Closing");
};
function sayPerson(){
    console.log("Sending Person!");
    sock.send("Person!");
}
sock.close()  -关闭连接
```
### WebSocket不支持 使用SockJS
>* 模拟
```java
registry.addHandler(webSocketHandler(), "/person").withSockJS();
```

XHR流,XDR流,iFrame事件流,iFrameHTML文件,XHR轮询,XDR轮询,iFrameXHR轮询,JSONP轮询

浏览器还要添加支持
```xml
<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
//改
var url = 'marco';
var sock = new SockJS(url);
```
---
### 使用STOMP
在Web Socket 之上提供了frame-based wire format层
1. 服务端
```java
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/person").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
// 当STOMP代理替换掉内存代理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        registry.setApplicationDestinationPrefixes("/app");
    }
```
2. 接收浏览器来的信息
```java
//spring bean中
@MessageMapping("/person") //   /app/person
public void handlePerson(Person person) {
    System.out.println(person);
}
// 应用场景:请求回应模式
// 当浏览器订阅時,消息原路返回,不经过代理
@SubscribeMapping("/person")
public Object handlePerson(Object person) {
    return new Object();
}
```
3. JS客户端
```js
var url = 'http://'+window.location.host+'/stomp/person';
var sock = new SockJS(url);   //('/person')
var stomp = Stomp.over(sock);
stomp.connect('username', 'password', function(frame){  //开启
});
stomp.subscribe('/topit/person',function(response){     //订阅
   JSON.parse(response.body) 
});
stomp.disconnect()              //关闭
stopm.send("/person"),{头信息},JSON.stringify({'name':'zzming'}));
```
4. 服务端想浏览器发送消息
```java
//1改返回类型
//2@SendTo注解重写默认目的地('/topic/person')
```
5. 使用SimpMessagingTemplate 发送消息
```java
template.convertAndSend("/topic/person", person);
```
6. 放送消息给特定用户
```
@SendToUser("/queue/...")  //回复用户
// 或
template.convertAndSendToUser("zzming","/topic/person", person);
```
客户端: stomp.subscribe("/user/queue/...",function(...))--多个/user
7. 处理异常
```java
@MessageExceptionHandler
@SendTo("/queue/errors")
public Object handleException(){...}
```




>* 小知识 HandleBars  见512




















