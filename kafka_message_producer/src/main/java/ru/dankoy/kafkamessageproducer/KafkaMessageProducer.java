package ru.dankoy.kafkamessageproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class KafkaMessageProducer {

    public static void main(String[] args) {
        SpringApplication.run(KafkaMessageProducer.class, args);
    }
}
