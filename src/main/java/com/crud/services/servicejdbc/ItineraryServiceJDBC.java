package com.crud.services.servicejdbc;

import com.crud.dtos.request.ItineraryRequest;
import com.crud.dtos.response.ItineraryResponse;
import com.crud.entities.ItineraryEntity;
import com.crud.entities.LocationEntity;
import com.crud.entities.OriginEntity;
import com.crud.exceptions.BusinessException;
import com.crud.mapper.ItineraryMapper;
import com.crud.reports.importexcel.HelperImportExcel;
import com.crud.repositories.ItineraryRepository;
import com.crud.repositories.LocationRepository;
import com.crud.repositories.OriginRepository;
import com.crud.repositories.jdbc.ItineraryRepositoryJDBC;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryServiceJDBC {

    private final ItineraryRepositoryJDBC itineraryRepositoryJDBC;
    private final ItineraryRepository repository;
    private final OriginRepository originRepository;
    private final LocationRepository locationRepository;
    private final ItineraryMapper mapper;

    @Cacheable(value = "itinerario")
    @Transactional(readOnly = true)
    public List<ItineraryResponse> getAllItineraryJDBC() {
        List<ItineraryResponse> itineraryEntities = itineraryRepositoryJDBC.listItinerary();

        if (itineraryEntities.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Itinerary");
        }
        return itineraryEntities;
    }



    @Transactional
    @CacheEvict(value = {"itinerario", "destino", "origen"}, allEntries = true)
    public ItineraryResponse createItinerary(ItineraryRequest itineraryRequest) {
        OriginEntity originEntity = mapper.toOriginEntity(itineraryRequest.getOrigin());
        LocationEntity locationEntity = mapper.toLocationEntity(itineraryRequest.getLocation());

        ItineraryEntity itineraryEntity = mapper.toEntity(itineraryRequest);
        itineraryEntity.setOrigin(originEntity);
        itineraryEntity.setLocation(locationEntity);

        originEntity.getItineraries().add(itineraryEntity);
        locationEntity.getItineraries().add(itineraryEntity);

        originRepository.save(originEntity);
        locationRepository.save(locationEntity);

        ItineraryEntity savedItinerary = repository.save(itineraryEntity);
        return mapper.toDto(savedItinerary);
    }

    public void save(MultipartFile file) {

        try {
            List<ItineraryRequest> itineraries = HelperImportExcel.convertExcelToListOfItinerary(file.getInputStream());

            List<ItineraryEntity> itineraryEntities = mapper.toEntityList(itineraries);
            repository.saveAll(itineraryEntities);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    @Transactional(readOnly = true)
    @CacheEvict(value = "itinerario")
    public ItineraryResponse getByIdItineraryJDBC(Long id){
        return itineraryRepositoryJDBC.getByIdItinerary(id);
    }
}

