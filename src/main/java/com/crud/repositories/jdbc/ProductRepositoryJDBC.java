package com.crud.repositories.jdbc;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.PageableResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.CategoryRepository;
import com.crud.repositories.jdbc.mapperjdbc.ProductMapperJDBC;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.crud.util.constants.ProductJdbcConstants.*;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryJDBC {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryRepository;

    ///............
    public long getCountOfProducts() {
        String countQuery = "SELECT COUNT(*) FROM Producto p INNER JOIN Categoria c ON p.category_id = c.id WHERE p.deleted = false AND c.deleted = false ";
        return jdbcTemplate.queryForObject(countQuery, Long.class);
    }

    public List<ProductResponse> getPaginatedProductsAndName(int offset, int pageSize, String productName) {
        String selectQuery = """
        SELECT  p.id,
                p.name,
                p.price,
                p.description,
                c.id as category_id,
                c.name as category_name
        FROM Producto p
        INNER JOIN Categoria c ON p.category_id = c.id
        WHERE p.deleted = false
        AND c.deleted = false
    """;
        List<Object> parameters = new ArrayList<>();

        if (productName != null) {
            selectQuery += " AND p.name LIKE ?";
            String searchName = "%" + productName + "%";
            parameters.add(searchName);
        }

        selectQuery += " ORDER BY p.id ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
        parameters.add(offset);
        parameters.add(pageSize);

        return jdbcTemplate.query(selectQuery, parameters.toArray(), new ProductMapperJDBC());
    }








    ///............

    public PageableResponse<ProductResponse> getAllPaginationProductJDBC(int pageNumber, int pageSize, String productName) {
        PageableResponse<ProductResponse> response = new PageableResponse<>();

        String countQuery = "SELECT COUNT(*) FROM Producto p INNER JOIN Categoria c ON p.category_id = c.id WHERE p.deleted = false AND c.deleted = false";
        String selectQuery = """
            SELECT  p.id,
                    p.name,
                    p.price,
                    p.description,
                    c.id as category_id,
                    c.name as category_name
            FROM Producto p
            INNER JOIN Categoria c ON p.category_id = c.id
            WHERE p.deleted = false
            AND c.deleted = false
            AND p.name LIKE ?
            ORDER BY p.id ASC
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;
            """;

        try {
            // Obtener el total de elementos
            String searchName = "%" + productName + "%";
            long totalElements = jdbcTemplate.queryForObject(countQuery, Long.class);
            response.setTotalElements(totalElements);

            // Calcular información de paginación
            int offset = (pageNumber - 1) * pageSize;

            // Ejecutar la consulta paginada
            List<ProductResponse> products = jdbcTemplate.query(selectQuery, new Object[]{searchName, offset, pageSize},
                    new ProductMapperJDBC());

            // Configurar los resultados en la respuesta
            response.setContent(products);
            response.setPageNumber(pageNumber);
            response.setPageSize(pageSize);
            response.setTotalPages((int) Math.ceil((double) response.getTotalElements() / pageSize));
            response.setLast(pageNumber == response.getTotalPages());
        } catch (DataAccessException e) {
            // Manejo de excepciones
        }

        return response;
    }








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
