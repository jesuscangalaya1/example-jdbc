package com.crud.repositories;

import com.crud.entities.ItineraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<ItineraryEntity, Long> {
}
