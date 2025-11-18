package com.example.kafkaavro.service;

import org.springframework.stereotype.Service;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class AggregatorService {
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicReference<Double> sum = new AtomicReference<>(0.0);


    public void updateAverage(float price) {
        sum.updateAndGet(v -> v + price);
        count.incrementAndGet();
    }


    public double getAverage() {
        int c = count.get();
        return c == 0 ? 0.0 : sum.get() / c;
    }
}