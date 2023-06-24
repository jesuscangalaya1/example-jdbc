package com.crud.services.servicejdbc;

import com.crud.dtos.request.CategoryRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.PageableResponse;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.jdbc.CategoryRepositoryJDBC;
import com.crud.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.crud.util.PaginationUtils.calculateOffset;

@Service
@RequiredArgsConstructor
public class CategoryServiceJDBC {

    private final CategoryRepositoryJDBC categoryRepositoryJDBC;


    public PageableResponse<CategoryResponse> getAllPaginationCategoryJDBC(int pageNumber, int pageSize) {
        // Validar pageNumber y pageSize utilizando PaginationUtils
        PaginationUtils.validatePaginationParameters(pageNumber, pageSize);

        // Obtener el total de elementos
        long totalElements = categoryRepositoryJDBC.getCountOfCategories();

        // Calcular información de paginación
        int offset = PaginationUtils.calculateOffset(pageNumber, pageSize);

        // Obtener las categorías paginadas
        List<CategoryResponse> categories = categoryRepositoryJDBC.getPaginatedCategories(offset, pageSize);

        // Validar si la lista de categorías está vacía
        PaginationUtils.validatePageContent(categories);

        // Validar si el número de página está dentro del rango válido
        int totalPages = PaginationUtils.calculateTotalPages(totalElements, pageSize);
        PaginationUtils.validatePageNumber(pageNumber, totalPages);

        // Validar el tamaño de página máximo
        int maxPageSize = 100; // Establecer el tamaño de página máximo permitido
        PaginationUtils.validatePageSize(pageSize, maxPageSize);

        // Configurar los resultados en la respuesta
        PageableResponse<CategoryResponse> response = new PageableResponse<>();
        response.setContent(categories);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalPages(totalPages);
        response.setTotalElements(totalElements);
        response.setLast(pageNumber == totalPages);

        return response;
    }




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
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        return categoryRepositoryJDBC.createCategory(categoryRequest);
    }

    @Transactional
    @CacheEvict(value = "Categoria", allEntries = true)
    public CategoryResponse updatedCategory(Long id, CategoryRequest categoryRequest) {
        return categoryRepositoryJDBC.updatedCategory(id, categoryRequest);
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = "Categoria")
    public CategoryResponse getByIdCategoryJDBC(Long id) {
        return categoryRepositoryJDBC.getByIdCategoryJDBC(id);
    }

    @Transactional
    @CacheEvict(value = "Categoria", allEntries = true)
    public void deleteByIdCategoryJDBC(Long id) {
        categoryRepositoryJDBC.deleteByIdCategory(id);
    }

}
