package com.example.CsvAnalyser.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.record.Country;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;

@Service
public class GeoIpService {


    public  String getCountryName(String ip){
        try {
            // Load the GeoLite2-Country.mmdb file
            InputStream databaseStream = GeoIpService.class
                    .getClassLoader()
                    .getResourceAsStream("GeoLite2-Country.mmdb");
            //File database = new File(GeoIpService.class.getClassLoader().getResource("GeoLite2-Country.mmdb").toURI());

            Country country;
            try (DatabaseReader dbReader = new DatabaseReader.Builder(databaseStream).build();) {

                // Replace with the IP address you want to look up
                InetAddress ipAddress = InetAddress.getByName(ip);

                // Get the country information
                country = dbReader.country(ipAddress).getCountry();
                return  country.getName();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            throw new RuntimeException(e);
        }
        return "Unknown Country";
    }

}


