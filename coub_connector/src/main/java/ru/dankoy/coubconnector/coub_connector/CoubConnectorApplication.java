package ru.dankoy.coubconnector.coub_connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CoubConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoubConnectorApplication.class, args);
	}

}
