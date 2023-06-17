package com.crud.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ItineraryResponse {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hour;

    private OriginResponse origin;
    private LocationResponse location;

}
