package com.example.earthquakeInformation.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EarthquakeInformation {

    private String place;
    private String magnitude;
    private LocalDateTime dateTime;


}
