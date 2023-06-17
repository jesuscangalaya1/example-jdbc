package com.crud.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
public class FlightRequest {

    private int capacity;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;

    private Double price;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime departureTime;

    private Long itineraryId;
}
