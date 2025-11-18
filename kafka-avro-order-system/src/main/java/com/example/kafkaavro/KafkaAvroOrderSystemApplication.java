package com.example.kafkaavro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaAvroOrderSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaAvroOrderSystemApplication.class, args);
	}
}