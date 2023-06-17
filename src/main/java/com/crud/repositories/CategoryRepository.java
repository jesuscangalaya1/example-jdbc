package com.crud.repositories;

import com.crud.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    //LLamar a todos, menos a los que han sido eliminados.(JPQL)
    List<CategoryEntity> findAllByDeletedFalse();

}
