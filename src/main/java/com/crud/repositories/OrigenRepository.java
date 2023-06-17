package com.crud.repositories;

import com.crud.entities.OriginEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrigenRepository extends JpaRepository<OriginEntity, Long> {
    Page<OriginEntity> findAllByDeletedFalse(Pageable pageable);

}
