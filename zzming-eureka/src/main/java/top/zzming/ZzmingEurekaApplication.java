package top.zzming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务发现组件eureka
 */
@SpringBootApplication
@EnableEurekaServer
public class ZzmingEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzmingEurekaApplication.class, args);
	}
}
