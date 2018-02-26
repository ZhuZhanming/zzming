package top.zzming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * spring cloud中的配置服务
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ZzmingConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzmingConfigApplication.class, args);
	}
}
