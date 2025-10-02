package com.example.CsvAnalyser.services;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class MetricsCollector {

    private final Map<String, Integer> invalidCounts = new ConcurrentHashMap<>();

    public void incrementInvalid(String column) {
        invalidCounts.merge(column, 1, Integer::sum);
    }

    public Map<String, Integer> getInvalidCounts() {
        return invalidCounts;
    }

    public void reset() {
        invalidCounts.clear();
    }
}

