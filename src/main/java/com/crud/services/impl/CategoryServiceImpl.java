package com.crud.services.impl;

import com.crud.dtos.request.CategoryRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.entities.CategoryEntity;
import com.crud.exceptions.BusinessException;
import com.crud.mapper.CategoryMapper;
import com.crud.repositories.CategoryRepository;
import com.crud.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.crud.util.AppConstants.BAD_REQUEST;
import static com.crud.util.AppConstants.BAD_REQUEST_CATEGORY;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "Categoria")
    @Transactional(readOnly = true)
    public List<CategoryResponse> listCategories() {
        List<CategoryEntity> productoEntities = categoryRepository.findAll();
        return Optional.of(productoEntities)
                .filter(list -> !list.isEmpty())
                .map(categoryMapper::categoryListToCategoryDtoList)
                .orElseThrow(() -> new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos"));
    }

    @Override
    @Cacheable(value = "Categoria")
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_CATEGORY + id));
        return categoryMapper.toDto(category);

 }

    @Override
    @CacheEvict(value = "Categoria", allEntries = true)
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoriaEntity = categoryMapper.toEntity(categoryRequest);
        CategoryEntity savedCategoryEntity = categoryRepository.save(categoriaEntity);
        return categoryMapper.toDto(savedCategoryEntity);
    }

    @Override
    @CacheEvict(value = "Categoria", allEntries = true)
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        CategoryEntity categoriaEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_CATEGORY + id));
        categoryMapper.updateCategoryFromDto(categoryRequest, categoriaEntity);
        categoriaEntity = categoryRepository.save(categoriaEntity);
        return categoryMapper.toDto(categoriaEntity);
    }


    @Override
    @CacheEvict(value = {"Categoria", "Producto"}, allEntries = true)
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_CATEGORY + id);
        }
        categoryRepository.deleteById(id);
    }
}
