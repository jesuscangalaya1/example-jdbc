package com.crud.controller;

import com.crud.dtos.request.ItineraryRequest;
import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.ItineraryResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.dtos.response.RestResponse;
import com.crud.reports.importexcel.HelperImportExcel;
import com.crud.repositories.jdbc.ItineraryRepositoryJDBC;
import com.crud.services.servicejdbc.ItineraryServiceJDBC;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.crud.util.AppConstants.MESSAGE_ID_PRODUCT;
import static com.crud.util.AppConstants.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itineraries")
@CrossOrigin("*")
public class ItineraryController {

    private final ItineraryServiceJDBC itineraryServiceJDBC;
    private static final Logger logger = LoggerFactory.getLogger(ItineraryController.class);


    // IMPORT EXCEL ITINERARY , ORIGIN AND LOCATION

    @PostMapping(value = "/import-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createItineraryJDBC(@RequestParam MultipartFile file) {
        try {
            logger.info("Received request to import Excel file.");

            // Process the Excel file and save data to the database
            itineraryServiceJDBC.save(file);

            logger.info("Data from the Excel file is saved to the database.");

            return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to the database"));
        } catch (Exception e) {
            logger.error("Error occurred while importing Excel file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while processing the file"));
        }
    }


    // JDBC ...

    @GetMapping(value = "/jdbc", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<List<ItineraryResponse>> getAllItineraryJDBC() {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "ITINERARY SUCCESSFULLY READED",
                itineraryServiceJDBC.getAllItineraryJDBC());
    }

    @PostMapping(value = "/jdbc")
    public RestResponse<ItineraryResponse> createItineraryJDBC(@RequestBody ItineraryRequest itineraryRequest) {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.CREATED),
                "ITINERARY SUCCESSFULLY CREATED",
                itineraryServiceJDBC.createItinerary(itineraryRequest));
    }

    @GetMapping(value = "/jdbc/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ItineraryResponse> getProductByIdJDBC(@PathVariable Long id) {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "ITINERARY ID: " + id + " SUCCESSFULLY READED",
                itineraryServiceJDBC.getByIdItineraryJDBC(id));
    }

}
