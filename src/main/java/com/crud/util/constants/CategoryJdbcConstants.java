package com.crud.util.constants;

public final class CategoryJdbcConstants {

    private CategoryJdbcConstants(){}


    public static final String SELECT_CATEGORIES_SQL =
            """
                 SELECT  c.id,
                         c.name,
                    FROM Categoria c
                    WHERE c.deleted = false;
                                      
                    """;
    public static final String CREATE_CATEGORY_SQL =
            """
                INSERT INTO Categoria (name)
                VALUES (?);
            """;

    public static final String GET_BY_ID_CATEGORY_SQL =
            """
                  SELECT c.id, c.name
                           FROM Categoria c
                           WHERE c.id = ?
                           AND c.deleted = false;
            """;

    public static final String UPDATED_CATEGORY_SQL =
            """
                UPDATE Categoria SET name = ?
                WHERE id = ?;
            """;

    public static final String DELETED_PRODUCTS_BY_CATEGORY_SQL =
            """
                UPDATE Producto SET deleted = true WHERE category_id = ?
            """;

    public static final String DELETED_CATEGORY_SQL =
            """
                UPDATE Categoria SET deleted = true WHERE id = ?
            """;

    public static final String COUNT_PRODUCTS_BY_CATEGORY_SQL =
            """
                SELECT COUNT(*) FROM Producto WHERE category_id = ?
            """;



}
