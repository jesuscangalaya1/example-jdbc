package com.crud.repositories;

import com.crud.entities.ItineraryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItinerarioRepository extends JpaRepository<ItineraryEntity, Long> {
    Page<ItineraryEntity> findAllByDeletedFalse(Pageable pageable);

}
