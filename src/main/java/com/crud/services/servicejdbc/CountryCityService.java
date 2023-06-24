package com.crud.services.servicejdbc;

import com.crud.dtos.response.OriginResponse;
import com.crud.entities.LocationEntity;
import com.crud.entities.OriginEntity;
import com.crud.repositories.LocationRepository;
import com.crud.repositories.OriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryCityService {

    private final OriginRepository originRepository;
    private final LocationRepository locationRepository;

    public List<String> searchOriginsAndDestinations(String searchTerm) {
        List<String> suggestions = new ArrayList<>();

        List<OriginEntity> origins = originRepository.findByCityContainingIgnoreCaseOrCountryContainingIgnoreCase(searchTerm, searchTerm);
        List<LocationEntity> destinations = locationRepository.findByCityContainingIgnoreCaseOrCountryContainingIgnoreCase(searchTerm, searchTerm);

        for (OriginEntity origin : origins) {
            suggestions.add(origin.getCity() + ", " + origin.getCountry());
        }

        for (LocationEntity destination : destinations) {
            suggestions.add(destination.getCity() + ", " + destination.getCountry());
        }

        return suggestions;
    }

    public List<OriginResponse> getAllCountrysAndCitys(){
        return null;
    }





}
