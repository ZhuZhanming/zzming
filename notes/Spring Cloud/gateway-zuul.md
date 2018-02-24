* 加依赖
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
```
* 加注解@EnableZuulProxy
>* 此时访问转发路径为:/服务名/请求路径
* 自定义路劲(更换服务名)
```yml
zuul:
  ignored-services: '*'  #要忽略的为服务
  routes:
    zzming-file: /file/**
```
* path+serviceId
```yml
zuul:
  routes:
    zzming-file: # 随便写
      path: /file/**
      serviceId: zzming-file
```
* path+url
```yml
zuul:
  routes:
    zzming-file: # 随便写
      path: /file/**
      url: http://localhost:7900/file/
```
* path+url 并负载均衡
```yml
zuul:
  routes:
    abc: # 随便写
      path: /file/**
      serviceId: zzming-file
ribbon:
  eureka:
    enabled: false
zzming-file: #这里是serviceId
  ribbon:
    listOfServers: http://localhost:7900/file,http://localhost:7901/file
```
* 使用正则
```java
// 
@Bean
public PatternServiceRouteMapper serviceRouteMapper() {
    return new PatternServiceRouteMapper(
        "(?<name>^.+)-(?<version>v.+$)",
        "${version}/${name}");
}
```
* strip-prefix
```yml
zuul:
  prefix: api #访问zuul路径从/file/**  =>  /api/file/**  (此时访问微服务的路径时/file/**)
  strip-prefix: false  #不忽略代理微服务的前缀(context-path);此时访问为服务的路径是/api/file/**


zuul:
  routes:
    abc: # 随便写
      path: /file/**
      serviceId: zzming-file
      strip-prefix: false
```