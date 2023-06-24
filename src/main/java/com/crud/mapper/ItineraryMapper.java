package com.crud.mapper;

import com.crud.dtos.request.ItineraryRequest;
import com.crud.dtos.request.LocationRequest;
import com.crud.dtos.request.OriginRequest;
import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.ItineraryResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.entities.ItineraryEntity;
import com.crud.entities.LocationEntity;
import com.crud.entities.OriginEntity;
import com.crud.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface ItineraryMapper {

    @Mapping(source = "origin", target = "origin" )
    @Mapping(source = "location", target = "location")
    ItineraryResponse toDto(ItineraryEntity itineraryEntity);

    @Mapping(target = "origin.city", source = "itineraryRequest.origin.city")
    @Mapping(target = "origin.country", source = "itineraryRequest.origin.country")
    @Mapping(target = "location.city", source = "itineraryRequest.location.city")
    @Mapping(target = "location.country", source = "itineraryRequest.location.country")
    ItineraryEntity toEntity(ItineraryRequest itineraryRequest);

    OriginEntity toOriginEntity(OriginRequest originRequest);

    LocationEntity toLocationEntity(LocationRequest locationRequest);

    List<ItineraryEntity> toEntityList(List<ItineraryRequest> itineraryEntityList);



}
