package com.example.earthquakeInformation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.earthquakeInformation.model.EarthquakeInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class EarthquakeController {

    @GetMapping(value = "/earthquakes")
    public List<EarthquakeInformation> getEarthquakes() throws JsonProcessingException {
        Scanner value = new Scanner(System.in);
        System.out.print("Enter a Start Date Year-Month-Day	  : ");
        String startDate = value.next();
        System.out.print("Enter a End Date Year-Month-Day	  : ");
        String endDate = value.next();
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+startDate+"&endtime="+endDate;
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        JsonNode jsonNode = objectMapper.readTree(restTemplate.getForObject(url, String.class));
        JsonNode features = jsonNode.get("features");
        List<EarthquakeInformation> resultList = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            if (!features.isEmpty()){
                EarthquakeInformation earthquake = new EarthquakeInformation();
                JsonNode node = features.get(i).get("properties");
                earthquake.setMagnitude(node.get("mag").asText());
                earthquake.setPlace(node.get("place").asText());
                earthquake.setDateTime(
                        Instant.ofEpochMilli(node.get("time").asLong()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                resultList.add(earthquake);
            } else {
                EarthquakeInformation earthquake = new EarthquakeInformation();
                earthquake.setMagnitude("Earthquake Not Found");
                earthquake.setPlace("Earthquake Not Found");
                earthquake.setDateTime(LocalDateTime.now());
                resultList.add(earthquake);
            }
        }

        return resultList;
    }
}
