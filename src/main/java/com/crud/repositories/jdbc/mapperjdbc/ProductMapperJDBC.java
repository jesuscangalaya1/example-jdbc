package com.crud.repositories.jdbc.mapperjdbc;

import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.ProductResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapperJDBC implements RowMapper<ProductResponse> {

    //SOLO SE USA CUANDO LAS NOMBRE DE LOS ATRIBUTOS NO COINCIDEN CON LA TABLA
    // AHY SERIA UTIL ESTO PERO SI TU DTO TIENES LOS MISMOS ATIRBUTOS USA EL BEANPROPERTY.

    @Override
    public ProductResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryResponse category = new CategoryResponse(
                rs.getLong("category_id"),
                rs.getString("category_name")
        );

        return new ProductResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("description"),
                category
        );
    }
}