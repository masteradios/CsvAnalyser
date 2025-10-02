package com.example.CsvAnalyser.services;

import com.example.CsvAnalyser.models.LogEntry;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class ReportWriter implements ItemWriter<LogEntry> {

    private int totalRows = 0;
    private final Map<String, Integer> countryCounts = new HashMap<>();
    private final Map<Integer, Integer> statusCounts = new HashMap<>();
    private final Set<String> uniquePaths = new HashSet<>();

    @Autowired
    private MetricsCollector metricsCollector;

    @PreDestroy
    public void generateReport() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log_report.txt"))) {
            writer.write("=== Log Analysis Report ===\n\n");
            writer.write("Total Rows Processed: " + totalRows + "\n\n");

            writer.write("Response Status Counts:\n");
            statusCounts.forEach((status, count) -> {
                try { writer.write(status + " : " + count + "\n"); } catch (Exception ignored) {}
            });
            writer.write("\n");

            writer.write("Country-wise IP Counts:\n");
            countryCounts.forEach((country, count) -> {
                try { writer.write(country + " : " + count + "\n"); } catch (Exception ignored) {}
            });
            writer.write("\n");

            writer.write("Unique Paths Accessed: " + uniquePaths.size() + "\n");


            System.out.println("Report generated: log_report.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Chunk<? extends LogEntry> chunk) throws Exception {

        for (LogEntry entry : chunk.getItems()) {
            totalRows++;
            countryCounts.merge(entry.getCountry(), 1, Integer::sum);
            statusCounts.merge(entry.getResponseStatus(), 1, Integer::sum);
            uniquePaths.add(entry.getPath());
        }
        Map<String, Integer> invalids = metricsCollector.getInvalidCounts();
        System.out.println("Invalid entries per column: " + invalids);
        try (PrintWriter metricsOut = new PrintWriter(new FileWriter("invalid_metrics.csv"))) {
            metricsOut.println("Column,InvalidCount");
            for (Map.Entry<String, Integer> e : invalids.entrySet()) {
                metricsOut.printf("%s,%d%n", e.getKey(), e.getValue());
            }
        }

    }
}

