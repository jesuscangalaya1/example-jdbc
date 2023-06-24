package com.crud.util.constants;

public final class ItineraryJdbcConstants {

    private ItineraryJdbcConstants() {
    }

    public static final String SELECT_ITINERARY_ORIGIN_LOCATION_SQL =
            """
                     SELECT  i.id, i.fecha_ida, i.fecha_salida, i.hora,
                             o.ciudad AS origen_ciudad, o.pais AS origen_pais,
                             d.ciudad AS destino_ciudad, d.pais AS destino_pais
                     FROM itinerario i
                     INNER JOIN origen o ON i.origen_id = o.id
                     INNER JOIN destino d ON i.destino_id = d.id
                     WHERE i.deleted = false
                        AND o.deleted = false
                        AND d.deleted = false;
                             
            """;

    public static final String GET_BY_ID_ITINERARY_SQL =
            """
                 SELECT i.id, i.fecha_ida, i.fecha_salida, i.hora,
                     o.ciudad AS origen_ciudad, o.pais AS origen_pais,
                     d.ciudad AS destino_ciudad, d.pais AS destino_pais
                 FROM itinerario i
                 INNER JOIN origen o ON i.origen_id = o.id
                 INNER JOIN destino d ON i.destino_id = d.id
                 WHERE i.id = ?
                 AND i.deleted = false
                 AND o.deleted = false
                 AND d.deleted = false;
                             
            """;



}
