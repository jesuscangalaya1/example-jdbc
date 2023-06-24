package com.crud.repositories.jdbc;

import com.crud.dtos.request.CategoryRequest;
import com.crud.dtos.response.CategoryResponse;

import com.crud.exceptions.BusinessException;
import com.crud.repositories.jdbc.mapperjdbc.CategoryMapperJDBC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.crud.util.constants.CategoryJdbcConstants.*;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryJDBC {

    private final JdbcTemplate jdbcTemplate;

    public long getCountOfCategories() {
        String countQuery = "SELECT COUNT(*) FROM Categoria WHERE deleted = false";
        Long count = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getLong(1));
        return count != null ? count : 0L;
    }

    public List<CategoryResponse> getPaginatedCategories(int offset, int pageSize) {
        String selectQuery = """
            SELECT c.id,
                   c.name
            FROM Categoria c
            WHERE c.deleted = false
            ORDER BY c.id ASC
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;
            """;
        return jdbcTemplate.query(selectQuery, ps -> {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
        }, new CategoryMapperJDBC());
    }


















    public List<CategoryResponse> getAllCategoryJDBC() {
        return jdbcTemplate.query(SELECT_CATEGORIES_SQL, new CategoryMapperJDBC());
    }




    public CategoryResponse getByIdCategoryJDBC(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("P-400", HttpStatus.BAD_REQUEST, "ID: " + id + "  de Categoria inválido");
        }

        List<CategoryResponse> categories = jdbcTemplate.query(GET_BY_ID_CATEGORY_SQL, new CategoryMapperJDBC(), id);
        if (categories.isEmpty()) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Categoria no encontrado");
        }
        return categories.get(0);
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Long generatedId = executeInsertStatement(categoryRequest);
        return getByIdCategoryJDBC(generatedId);
    }

    public CategoryResponse updatedCategory(Long id, CategoryRequest categoryRequest) {
        getByIdCategoryJDBC(id);
        // Actualizar el producto en la base de datos
        int rowsAffected = jdbcTemplate.update(
                UPDATED_CATEGORY_SQL,
                categoryRequest.getName(),
                id
        );
        if (rowsAffected == 0) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Categoria no encontrado");
        }
        return getByIdCategoryJDBC(id);
    }

    public void deleteByIdCategory(Long id) {
        // Verificar si la categoría existe
        getByIdCategoryJDBC(id);

        // Verificar si existen productos relacionados a la categoría
        int relatedProductsCount = jdbcTemplate.queryForObject(COUNT_PRODUCTS_BY_CATEGORY_SQL, Integer.class, id);
        if (relatedProductsCount > 0) {
            // Actualizar el atributo deleted a true en los productos relacionados
            jdbcTemplate.update(DELETED_PRODUCTS_BY_CATEGORY_SQL, id);
        }

        // Actualizar el atributo deleted a true en la tabla Categoria
        int rowsAffected = jdbcTemplate.update(DELETED_CATEGORY_SQL, id);
        if (rowsAffected == 0) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }
    }






    // Separando de CreateCategory para tener un codigo bien estructurado.
    private Long executeInsertStatement(CategoryRequest categoryRequest) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_CATEGORY_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categoryRequest.getName());
            return ps;
        }, keyHolder);

        return (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
    }


}
