#### 前提
* 导入eureka和security的start包
* 启动类上@EnableEurekaServer

#### 单例
* application.yml文件
```yml
eureka:
  client:
    service-url:
      defaultZone: http://user:password123@localhost:8761/eureka
    fetch-registry: false
    register-with-eureka: false
server:
  port: 8761
security:
  user:
    name: user
    password: password123
```
