package com.crud.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse {
    private Long id;
    private String city;
    private String country;
}
