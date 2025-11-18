package com.example.kafkaavro.consumer;

import com.example.kafkaavro.avro.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class RetryConsumer {


    private final KafkaTemplate<String, Order> kafkaTemplate;


    @Value("${app.topics.main}")
    private String mainTopic;


    @Value("${app.topics.dlq}")
    private String dlqTopic;


    private final ConcurrentHashMap<String, AtomicInteger> retryCounts = new ConcurrentHashMap<>();
    private final int MAX_RETRIES = 3;


    public RetryConsumer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "${app.topics.retry}", groupId = "retry-consumer")
    public void listen(Order order) {
        String id = order.getOrderId().toString();
        AtomicInteger count = retryCounts.computeIfAbsent(id, k -> new AtomicInteger(0));
        int attempt = count.incrementAndGet();


        if (attempt <= MAX_RETRIES) {
            System.out.println("Retrying (attempt " + attempt + ") -> " + order);
// re-publish to main topic to be reprocessed
            kafkaTemplate.send(mainTopic, id, order);
        } else {
            System.out.println("Max retries reached. Sending to DLQ -> " + order);
            kafkaTemplate.send(dlqTopic, id, order);
            retryCounts.remove(id);
        }
    }
}