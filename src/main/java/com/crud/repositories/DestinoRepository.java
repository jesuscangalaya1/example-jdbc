package com.crud.repositories;

import com.crud.entities.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinoRepository extends JpaRepository<LocationEntity, Long> {
    Page<LocationEntity> findAllByDeletedFalse(Pageable pageable);

}
