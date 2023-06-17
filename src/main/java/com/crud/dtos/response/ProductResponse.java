package com.crud.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private CategoryResponse category;

    private Integer image;
    public ProductResponse(Long id, String name, Double price, String description, CategoryResponse category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }


}
