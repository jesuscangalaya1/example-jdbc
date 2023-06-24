package com.crud.repositories.jdbc;

import com.crud.dtos.request.ItineraryRequest;
import com.crud.dtos.response.ItineraryResponse;
import com.crud.exceptions.BusinessException;
import com.crud.repositories.jdbc.mapperjdbc.ItineraryMapperJDBC;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;

import static com.crud.util.constants.ItineraryJdbcConstants.*;

@Service
@RequiredArgsConstructor
public class ItineraryRepositoryJDBC {

    private final JdbcTemplate jdbcTemplate;

    public List<ItineraryResponse> listItinerary() {
        return jdbcTemplate.query(SELECT_ITINERARY_ORIGIN_LOCATION_SQL, new ItineraryMapperJDBC());
    }

    public ItineraryResponse getByIdItinerary(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("P-400", HttpStatus.BAD_REQUEST, "ID: " + id + "  de Itinerary inválido");
        }
        List<ItineraryResponse> itinerary = jdbcTemplate.query(GET_BY_ID_ITINERARY_SQL, new ItineraryMapperJDBC(), id);
        if (itinerary.isEmpty()) {
            throw new BusinessException("P-404", HttpStatus.NOT_FOUND, "Itinerary no encontrado");
        }
        return itinerary.get(0);
    }


}
