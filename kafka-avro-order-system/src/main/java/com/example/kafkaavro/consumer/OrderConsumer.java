package com.example.kafkaavro.consumer;

import com.example.kafkaavro.avro.Order;
import com.example.kafkaavro.service.AggregatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class OrderConsumer {


    private final AggregatorService aggregatorService;
    private final KafkaTemplate<String, Order> kafkaTemplate;


    @Value("${app.topics.retry}")
    private String retryTopic;


    public OrderConsumer(AggregatorService aggregatorService, KafkaTemplate<String, Order> kafkaTemplate) {
        this.aggregatorService = aggregatorService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "${app.topics.main}", groupId = "order-consumer")
    public void listen(Order order) {
        try {
// Update running average
            aggregatorService.updateAverage(order.getPrice());
            System.out.printf("Consumed: %s | RunningAvg: %.2f\n", order, aggregatorService.getAverage());


// Simulate transient failure for high prices
            if (order.getPrice() > 100) {
                throw new RuntimeException("Simulated transient failure for price > 100");
            }


        } catch (Exception e) {
// send to retry topic
            kafkaTemplate.send(retryTopic, order.getOrderId().toString(), order);
            System.out.println("Sent to retry: " + order);
        }
    }
}