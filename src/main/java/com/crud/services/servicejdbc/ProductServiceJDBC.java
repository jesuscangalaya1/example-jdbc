package com.crud.services.servicejdbc;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.ProductResponse;
import com.crud.entities.ProductEntity;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.CategoryRepository;
import com.crud.repositories.jdbc.ProductRepositoryJDBC;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceJDBC {

    private final ProductRepositoryJDBC productRepositoryJDBC;

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
    public ProductResponse createProduct(ProductRequest productRequest){
        return productRepositoryJDBC.createProduct(productRequest);
    }

    @Transactional
    @CacheEvict(value = "Producto", allEntries = true)
    public ProductResponse updatedProduct(Long id, ProductRequest productRequest){
        return productRepositoryJDBC.updatedProduct(id, productRequest);
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = "Producto")
    public ProductResponse getByIdProductJDBC(Long id){
        return productRepositoryJDBC.getByIdProduct(id);
    }

    @Transactional
    @CacheEvict(value = "Producto", allEntries = true)
    public void deleteByIdProductJDBC(Long id){
        productRepositoryJDBC.deleteByIdProduct(id);
    }
}
