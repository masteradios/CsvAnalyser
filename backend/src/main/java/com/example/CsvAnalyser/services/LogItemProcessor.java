package com.example.CsvAnalyser.services;

import com.example.CsvAnalyser.models.LogEntry;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class LogItemProcessor implements ItemProcessor<LogEntry, LogEntry> {


    @Autowired
    private GeoIpService geoIpService;

    @Override
    public LogEntry process(LogEntry logEntry) throws Exception {
        try {
            String countryName = geoIpService.getCountryName(logEntry.getIp());
            logEntry.setCountry(countryName);
        } catch (Exception e) {
            System.out.println("error came");
            System.out.println(e);
            logEntry.setCountry("UNKNOWN");
        }
        return logEntry;
    }
}