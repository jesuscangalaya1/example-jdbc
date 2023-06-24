package com.crud.services.servicejdbc;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.PageableResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.entities.ProductEntity;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.CategoryRepository;
import com.crud.repositories.jdbc.ProductRepositoryJDBC;
import com.crud.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceJDBC {

    private final ProductRepositoryJDBC productRepositoryJDBC;

    @Cacheable(value = "Producto")
    @Transactional(readOnly = true)
    public PageableResponse<ProductResponse> getAllPaginationProducts(int pageNumber, int pageSize, String productName) {

        // Validar pageNumber y pageSize utilizando PaginationUtils
        PaginationUtils.validatePaginationParameters(pageNumber, pageSize);

        // Obtener el total de elementos
        long totalElements = productRepositoryJDBC.getCountOfProducts();

        // Calcular información de paginación
        int offset = PaginationUtils.calculateOffset(pageNumber, pageSize);

        // Obtener los productos paginados
        List<ProductResponse> products = productRepositoryJDBC.getPaginatedProductsAndName(offset, pageSize, productName);


        // Validar si la lista de productos está vacía
        PaginationUtils.validatePageContent(products);

        // Validar si el número de página está dentro del rango válido
        int totalPages = PaginationUtils.calculateTotalPages(totalElements, pageSize);
        PaginationUtils.validatePageNumber(pageNumber, totalPages);

        // Validar el tamaño de página máximo
        int maxPageSize = 100; // Establecer el tamaño de página máximo permitido
        PaginationUtils.validatePageSize(pageSize, maxPageSize);

        // Configurar los resultados en la respuesta
        PageableResponse<ProductResponse> response = new PageableResponse<>();
        response.setContent(products);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalPages(totalPages);
        response.setTotalElements(totalElements);
        response.setLast(pageNumber == totalPages);

        return response;
    }



    @Cacheable(value = "Producto")
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductJDBC() {
        List<ProductResponse> productEntities = productRepositoryJDBC.getAllProductJDBC();

        if (productEntities.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos");
        }
        return productEntities;
    }

    @Transactional
    @CacheEvict(value = "Producto", allEntries = true)
    public ProductResponse createProduct(ProductRequest productRequest) {
        return productRepositoryJDBC.createProduct(productRequest);
    }

    @Transactional
    @CacheEvict(value = "Producto", allEntries = true)
    public ProductResponse updatedProduct(Long id, ProductRequest productRequest) {
        return productRepositoryJDBC.updatedProduct(id, productRequest);
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = "Producto")
    public ProductResponse getByIdProductJDBC(Long id) {
        return productRepositoryJDBC.getByIdProduct(id);
    }

    @Transactional
    @CacheEvict(value = "Producto", allEntries = true)
    public void deleteByIdProductJDBC(Long id) {
        productRepositoryJDBC.deleteByIdProduct(id);
    }
}
