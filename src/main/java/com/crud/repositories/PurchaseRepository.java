package com.crud.repositories;

import com.crud.entities.PurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    Page<PurchaseEntity> findAllByDeletedFalse(Pageable pageable);

}
