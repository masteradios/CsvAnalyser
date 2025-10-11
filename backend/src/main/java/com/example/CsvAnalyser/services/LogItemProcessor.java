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
            String ip = logEntry.getIp();
            System.out.println("prinitng "+ ip);
            // Skip empty, header, or malformed IPs
            if (ip == null || ip.equalsIgnoreCase("IP") || ip.equals("-")) {
                logEntry.setCountry("UNKNOWN");
                return logEntry;
            }

            String countryName = geoIpService.getCountryName(ip);
            logEntry.setCountry(countryName != null ? countryName : "UNKNOWN");

        } catch (Exception e) {
            System.out.println("error came");
            e.printStackTrace();
            logEntry.setCountry("UNKNOWN");
        }
        return logEntry;
    }

}