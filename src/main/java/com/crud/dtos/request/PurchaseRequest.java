package com.crud.dtos.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {

    private Integer amount;
    private Double price;
    private Double total;
    private Long flightId;


}
