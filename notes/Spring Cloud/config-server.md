* 查找规则
>* /{application}/{profile}[/{label}]
>* /{application}-{profile}.yml
>* /{label}/{application}-{profile}.yml
>* /{application}-{profile}.properties
>* /{label}/{application}-{profile}.properties
* 基本使用  一个微服务对应一个仓库
>* Placeholders in Git URI
```
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/ZhuZhanming/{application}
```
* 模式匹配(Pattern Matching and Multiple Repositories)
```
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          repos:
            simple: https://github.com/simple/config-repo
            special:
              #匹配不到找公用的
              pattern: special*/dev*,*special*/dev*
              uri: https://github.com/special/config-repo
            local:
              pattern: local*
              uri: file:/home/configsvc/config-repo
```
* 另一种常用用法--增加查找路经
```
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          searchPaths: foo,bar*
```
* clone-on-start不配在第一次请求时下载
* 加解密中yml加'    properties不加
#### 安全
* 加security的jar包
* 同eureka
#### 手动刷新
* @RefreshScope
* actu...jar包
* curl -X POST http://localhost:8888/refresh
#### 自动刷新
* 安装RabbitMQ
* 加RabbitMQ-bus依赖
* 连上RabbitMQ