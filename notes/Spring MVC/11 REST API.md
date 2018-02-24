### REST替换SOAP
* 匹配CRUD动作
>* Create:Post
>* Read:GET
>* Update:PUT或PATCH
>* Delete:DELETE
* 内容协商(Content negotiation):将模型渲染成一个视图
* 消息转换器(message conversion):将返回对象转换...
### 1.ContentNegotiatingViewResolver的工作原理
1. 扩展名:.json,.xml,.html
2. accept头部信息(都没有->'/':任何形式的表述)
3. 将逻辑视图名解析为渲染模型的View
4. 遍历其他视图解析的View,放到列表中
5. 循环客户端请求的所有媒体类型,使用第一个匹配的视图来渲染模型
6. 缺点
>* 人类接口和非人类接口不重叠
>* 不能处理JSON或XML
>* 给客户端的是渲染后的模型而不是资源
```java
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
}
```
### 2.消息转换器
特点:无模型,无视图,有数据和消息转换器
* 自动注册(HttpMessageConverter) 类路径上存在对应的库
>* AtomFeed            Rome Feed对象和 Atom feed
>* Jaxb2RootElement    使用JAXB2注解的对象和XML
>* MappingJackson2     json和类型化对象和HashMap
>* RssChannel          Rome Channel对象和RSS feed
* @ResponseBody 和 produces="application/json"(406 Noot Accptable)
* @RequestBody 和 consumes="application/json"
#### 高级用法--提供额外的信息(eg.错误)
```java
//使用ResponseEntity--不需要调加@ResponseBody注解
HttpStatus status = obj != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
return new ResponseEntity<Object>(obj, status);
//使用@ResonseStatus
```
#### 相应中设置头部信息
```java
//硬编码
HttpHeaders headers = new HttpHeaders();
headers.setLocation(URI.create("http://local..."));
return new ResponseEntity<>(obj, headers,HttpStatus.CREATED);

//使用UriComponents
...(UriComponentsBuilder ucb){
   Uri location = ucb.path("/person/").path("30").build().toUri(); 
   ...
}
```
### REST客户端
* RestTemplate
> 11个独立方法,其中10个重载3次,第11个重载6次
```java
rest.getForObject("http//.../{id}", Object.class, id);
rest.getForEntity(...).getHeader().get
rest.postForLocation(url, request)
//交换资源
MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
map.set("Accept", "application/json");
HttpEntity<Object> entity = new HttpEntity<>(map);
rest.exchange(entity, responseType);
```
