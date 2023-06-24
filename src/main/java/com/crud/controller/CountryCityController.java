package com.crud.controller;

import com.crud.services.servicejdbc.CountryCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countrys-citys")
@CrossOrigin("*")
public class CountryCityController {

    private final CountryCityService service;

    @GetMapping("/search")
    public List<String> searchOriginsAndDestinations(@RequestParam("term") String searchTerm) {
        return service.searchOriginsAndDestinations(searchTerm);
    }
}
