package com.crud.util;

public final class JdbcConstants {

    private JdbcConstants() {}

    //  QUERYS LIST JDBC..

    public static final String SELECT_PRODUCTS_SQL =
            """
                    SELECT  p.id,
                            p.name,
                            p.price,
                            p.description,
                            c.id as category_id,
                            c.name as category_name
                    FROM Producto p
                    INNER JOIN Categoria c ON p.category_id = c.id
                    WHERE p.deleted = false
                    AND c.deleted = false;
                                      
                    """;
    public static final String CREATE_PRODUCTS_SQL =
            """
                INSERT INTO Producto (name, price, description, category_id)
                VALUES (?, ?, ?, ?);
            """;

    public static final String GET_BY_ID_PRODUCT_SQL =
            """
                  SELECT p.id, p.name, p.price, p.description, c.id as category_id, c.name as category_name 
                           FROM Producto p INNER JOIN Categoria c ON p.category_id = c.id 
                           WHERE p.id = ?
                           AND p.deleted = false
            """;


    public static final String UPDATED_PRODUCTS_SQL =
            """
                UPDATE Producto SET name = ?, price = ?, description = ?, category_id = ?
                WHERE id = ?;
            """;


    public static final String DELETED_BY_ID_PRODUCTS_SQL =
            """
                UPDATE Producto SET deleted = true WHERE id = ?
                                 
                             
            """;







     }
