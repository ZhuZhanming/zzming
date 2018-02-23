package top.zzming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ZzmingEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzmingEurekaApplication.class, args);
	}
}
