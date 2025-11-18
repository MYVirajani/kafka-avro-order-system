package com.example.kafkaavro.consumer;

import com.example.kafkaavro.avro.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class DLQConsumer {


    @KafkaListener(topics = "${app.topics.dlq}", groupId = "dlq-consumer")
    public void listen(Order order) {
        System.out.println("DLQ received: " + order);

    }
}