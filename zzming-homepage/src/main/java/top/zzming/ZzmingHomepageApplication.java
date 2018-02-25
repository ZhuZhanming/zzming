package top.zzming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
public class ZzmingHomepageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzmingHomepageApplication.class, args);
	}

}
