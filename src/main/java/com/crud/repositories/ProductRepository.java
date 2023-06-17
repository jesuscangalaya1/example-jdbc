package com.crud.repositories;

import com.crud.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    //LLamar a todos, menos a los que han sido eliminados.(JPQL)
    Page<ProductEntity> findAllByDeletedFalse(Pageable pageable);

}
