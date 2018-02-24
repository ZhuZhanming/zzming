1. 导包
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
3. 配置
```yml
spring:
  mail:
    host: smtp.qq.com
    username: 1020030139@qq.com
    password: zpwtksnavzmqbeii
    port: 587
    properties:
      mail: 
        smtp: 
          auth: true
          timeout: 25000
```
2. 注入JavaMailSender使用

2.1 简单使用
```java
SimpleMailMessage message = new SimpleMailMessage();
message.setFrom("1020030139@qq.com");
message.setTo("13541525290@163.com");
message.setSubject("Hello  163");
message.setText("我去年买了个表  哈哈啊哈哈哈");
mailSender.send(message);
```
2.2 添加附件
```java
//Multipurpose Internet Mail Extensions
MimeMessage message = mailSender.createMimeMessage();
MimeMessageHelper helper= new MimeMessageHelper(message, true);
helper.setFrom("1020030139@qq.com");
helper.setTo("13541525290@163.com");
helper.setSubject("Multipurpose Internet Mail Extensions");
helper.setText("我几乎沙鲁克汗金砂东路看");
FileSystemResource image = new FileSystemResource("information_out.png");
helper.addAttachment("information_out.png", image);
mailSender.send(message);
```
2.3 富文本
```java
//Multipurpose Internet Mail Extensions
MimeMessage message = mailSender.createMimeMessage();
MimeMessageHelper helper= new MimeMessageHelper(message, true);
helper.setFrom("1020030139@qq.com");
helper.setTo("13541525290@163.com");
helper.setSubject("Multipurpose Internet Mail Extensions");
helper.setText("<html><img src='cid:information_out'></html>",true);//*
FileSystemResource image = new FileSystemResource("information_out.png");
helper.addInline("information_out", image);  //*
mailSender.send(message);
```
2.4 使用模板
```导包
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity</artifactId>
    <version>1.7</version>
</dependency>
```