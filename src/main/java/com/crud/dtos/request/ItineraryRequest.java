package com.crud.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ItineraryRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hour;

    private Long originId;
    private Long locationId;
}
