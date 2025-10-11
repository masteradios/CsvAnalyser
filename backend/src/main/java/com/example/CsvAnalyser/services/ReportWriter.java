package com.example.CsvAnalyser.services;

import com.example.CsvAnalyser.models.LogEntry;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class ReportWriter implements ItemWriter<LogEntry> {

    private int totalRows = 0;
    private final String outputFolder;
    private final Map<String, Integer> countryCounts = new HashMap<>();
    private final Map<Integer, Integer> statusCounts = new HashMap<>();
    private final Set<String> uniquePaths = new HashSet<>();

    @Autowired
    private MetricsCollector metricsCollector;
    public ReportWriter(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @PreDestroy
    public void generateReport() {
        File outputFolderDir = new File(outputFolder);
        if (!outputFolderDir.exists()) {
            outputFolderDir.mkdirs();  // create directories recursively
        }
        File metricsFile = new File(outputFolder, "log_report.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(metricsFile))) {
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
            statusCounts.merge(entry.getResponse(), 1, Integer::sum);
            uniquePaths.add(entry.getPath());
        }
        Map<String, Integer> invalids = metricsCollector.getInvalidCounts();
        System.out.println("Invalid entries per column: " + invalids);
        File outputFolderDir = new File(outputFolder);
        if (!outputFolderDir.exists()) {
            outputFolderDir.mkdirs();  // create directories recursively
        }
        File invalidOutputFile = new File(outputFolder, "invalid_metrics.csv");
        try (PrintWriter metricsOut = new PrintWriter(new FileWriter(invalidOutputFile))) {
            metricsOut.println("Column,InvalidCount");
            for (Map.Entry<String, Integer> e : invalids.entrySet()) {
                metricsOut.printf("%s,%d%n", e.getKey(), e.getValue());
            }
        }

    }
}





