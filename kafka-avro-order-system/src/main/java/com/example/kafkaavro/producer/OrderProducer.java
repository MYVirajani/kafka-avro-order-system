package com.example.kafkaavro.producer;

import com.example.kafkaavro.avro.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class OrderProducer {


    private final KafkaTemplate<String, Order> kafkaTemplate;


    @Value("${app.topics.main}")
    private String ordersTopic;


    public OrderProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    @Scheduled(fixedRate = 1000)
    public void produce() {
        String id = UUID.randomUUID().toString();
//        String id = "100" + ThreadLocalRandom.current().nextInt(1, 5);
        String product = "Item" + ThreadLocalRandom.current().nextInt(1, 5);
        float price = ThreadLocalRandom.current().nextFloat() * 120; // 0..120


        Order order = Order.newBuilder()
                .setOrderId(id)
                .setProduct(product)
                .setPrice(price)
                .build();


        kafkaTemplate.send(ordersTopic, id, order);
        System.out.println("Produced: " + order);
    }
}