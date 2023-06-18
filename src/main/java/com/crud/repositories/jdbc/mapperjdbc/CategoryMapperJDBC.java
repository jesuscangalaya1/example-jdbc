package com.crud.repositories.jdbc.mapperjdbc;

import com.crud.dtos.response.CategoryResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapperJDBC implements RowMapper<CategoryResponse> {


    @Override
    public CategoryResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CategoryResponse(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
