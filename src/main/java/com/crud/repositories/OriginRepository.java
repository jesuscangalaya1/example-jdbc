package com.crud.repositories;

import com.crud.entities.OriginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OriginRepository extends JpaRepository<OriginEntity, Long> {

    List<OriginEntity> findByCityContainingIgnoreCaseOrCountryContainingIgnoreCase(String searchTerm, String searchTerm1);
}
