package com.crud.reports.importexcel;

import com.crud.dtos.request.ItineraryRequest;
import com.crud.dtos.request.LocationRequest;
import com.crud.dtos.request.OriginRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelperImportExcel {

    //check that file is of excel type or not
    public static boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }

    }




    // Convierte el archivo Excel a una lista de objetos ItineraryRequest
    public static List<ItineraryRequest> convertExcelToListOfItinerary(InputStream is) {
        List<ItineraryRequest> list = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("data");

            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();
                ItineraryRequest itineraryRequest = new ItineraryRequest();

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int cid = cell.getColumnIndex();

                    switch (cell.getCellType()) {
                        case STRING -> {
                            switch (cid) {
                                case 0 -> itineraryRequest.setDepartureDate(LocalDate.parse(cell.getStringCellValue()));
                                case 1 -> itineraryRequest.setArrivalDate(LocalDate.parse(cell.getStringCellValue()));
                                case 2 -> itineraryRequest.setHour(cell.getStringCellValue());

                                default -> {
                                }
                            }
                        }
                        case NUMERIC -> {
                            switch (cid) {
                                case 0 -> itineraryRequest.setDepartureDate(cell.getLocalDateTimeCellValue().toLocalDate());
                                case 1 -> itineraryRequest.setArrivalDate(cell.getLocalDateTimeCellValue().toLocalDate());
                                // Aquí puedes manejar el formato de la hora si es necesario
                                default -> {
                                }
                            }
                        }
                        default -> {
                        }
                        // Manejar otros tipos de celdas si es necesario
                    }
                    cid++;
                }

                list.add(itineraryRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



}
