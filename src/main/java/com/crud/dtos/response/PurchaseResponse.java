package com.crud.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PurchaseResponse {

    private Long id;
    private Integer amount;
    private Double price;
    private Double total;
    //private LocalDate fecha;
    private List<FlightResponse> flights = new ArrayList<>();
}
