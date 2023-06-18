package com.crud.services.servicejdbc;

import com.crud.dtos.request.CategoryRequest;
import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.jdbc.CategoryRepositoryJDBC;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceJDBC {

    private final CategoryRepositoryJDBC categoryRepositoryJDBC;

    @Cacheable(value = "Categoria")
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoryJDBC() {
        List<CategoryResponse> categoryEntities = categoryRepositoryJDBC.getAllCategoryJDBC();

        if (categoryEntities.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Categorias");
        }
        return categoryEntities;
    }

    @Transactional
    @CacheEvict(value = "Categoria", allEntries = true)
    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        return categoryRepositoryJDBC.createCategory(categoryRequest);
    }

    @Transactional
    @CacheEvict(value = "Categoria", allEntries = true)
    public CategoryResponse updatedCategory(Long id, CategoryRequest categoryRequest){
        return categoryRepositoryJDBC.updatedCategory(id, categoryRequest);
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = "Categoria")
    public CategoryResponse getByIdCategoryJDBC(Long id){
        return categoryRepositoryJDBC.getByIdCategoryJDBC(id);
    }

    @Transactional
    @CacheEvict(value = "Categoria", allEntries = true)
    public void deleteByIdCategoryJDBC(Long id){
        categoryRepositoryJDBC.deleteByIdCategory(id);
    }

}
