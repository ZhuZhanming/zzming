package top.zzming;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("top.zzming.dao")
public class ZzmingUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzmingUserApplication.class, args);
	}
}
