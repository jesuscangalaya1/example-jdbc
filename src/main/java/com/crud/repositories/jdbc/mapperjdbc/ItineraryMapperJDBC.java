package com.crud.repositories.jdbc.mapperjdbc;

import com.crud.dtos.response.ItineraryResponse;
import com.crud.dtos.response.LocationResponse;
import com.crud.dtos.response.OriginResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItineraryMapperJDBC implements RowMapper<ItineraryResponse> {

    @Override
    public ItineraryResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItineraryResponse itineraryResponse = new ItineraryResponse();
        itineraryResponse.setId(rs.getLong("id"));
        itineraryResponse.setDepartureDate(rs.getDate("fecha_ida").toLocalDate());
        itineraryResponse.setArrivalDate(rs.getDate("fecha_salida").toLocalDate());
        itineraryResponse.setHour(rs.getString("hora"));

        OriginResponse originResponse = new OriginResponse();
        originResponse.setId(rs.getLong("id"));
        originResponse.setCity(rs.getString("origen_ciudad"));
        originResponse.setCountry(rs.getString("origen_pais"));
        itineraryResponse.setOrigin(originResponse);

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(rs.getLong("id"));
        locationResponse.setCity(rs.getString("destino_ciudad"));
        locationResponse.setCountry(rs.getString("destino_pais"));
        itineraryResponse.setLocation(locationResponse);

        return itineraryResponse;
    }
}

