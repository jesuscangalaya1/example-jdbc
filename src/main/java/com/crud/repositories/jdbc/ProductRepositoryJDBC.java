package com.crud.repositories.jdbc;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.ProductResponse;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.CategoryRepository;
import com.crud.repositories.jdbc.mapperjdbc.ProductMapperJDBC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.crud.util.constants.ProductJdbcConstants.*;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryJDBC {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> getAllProductJDBC() {
        return jdbcTemplate.query(SELECT_PRODUCTS_SQL, new ProductMapperJDBC());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Long generatedId = executeInsertStatement(productRequest);
        return getByIdProduct(generatedId);
    }


    public ProductResponse updatedProduct(Long id, ProductRequest productRequest) {
        // Verificar si el producto existe
        getByIdProduct(id);

        // Verificar si el ID de categoría es válido
        if (!categoryRepository.existsById(productRequest.getCategoryId())) {
            throw new BusinessException("P-400", HttpStatus.BAD_REQUEST, "ID: " + productRequest.getCategoryId() + "  de categoría inválido");
        }

        // Actualizar el producto en la base de datos
        int rowsAffected = jdbcTemplate.update(
                UPDATED_PRODUCTS_SQL,
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getDescription(),
                productRequest.getCategoryId(),
                id
        );
        if (rowsAffected == 0) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Producto no encontrado");
        }

        return getByIdProduct(id);

    }

    public ProductResponse getByIdProduct(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("P-400", HttpStatus.BAD_REQUEST, "ID: "+id+ "  de producto inválido");
        }

        List<ProductResponse> products = jdbcTemplate.query(GET_BY_ID_PRODUCT_SQL, new ProductMapperJDBC(), id);
        if (products.isEmpty()) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        return products.get(0);
    }

    public void deleteByIdProduct(Long id) {
        // Verificar si el producto existe
        getByIdProduct(id);

        int rowsAffected = jdbcTemplate.update(DELETED_BY_ID_PRODUCTS_SQL, id);
        if (rowsAffected == 0) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
    }












    // Separando de CreateProduct para tener un codigo bien estructurado.
    private Long executeInsertStatement(ProductRequest productRequest) {
        if (!categoryRepository.existsById(productRequest.getCategoryId())) {
            throw new BusinessException("P-400", HttpStatus.BAD_REQUEST, "ID: " + productRequest.getCategoryId() + "  de categoría inválido");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_PRODUCTS_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, productRequest.getName());
            ps.setDouble(2, productRequest.getPrice());
            ps.setString(3, productRequest.getDescription());
            ps.setLong(4, productRequest.getCategoryId());
            return ps;
        }, keyHolder);

        return (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
    }


}
