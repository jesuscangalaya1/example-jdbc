package com.crud.repositories;

import com.crud.entities.FlightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VueloRepository extends JpaRepository<FlightEntity, Long> {
    Page<FlightEntity> findAllByDeletedFalse(Pageable pageable);

}
