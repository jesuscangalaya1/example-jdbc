package com.crud.repositories;

import com.crud.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    List<LocationEntity> findByCityContainingIgnoreCaseOrCountryContainingIgnoreCase(String searchTerm, String searchTerm1);
}
