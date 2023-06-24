package com.crud.controller;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.PageableResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.dtos.response.RestResponse;
import com.crud.repositories.jdbc.ProductRepositoryJDBC;
import com.crud.services.ProductService;
import com.crud.services.servicejdbc.ProductServiceJDBC;
import com.crud.util.AppConstants;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.crud.util.AppConstants.MESSAGE_ID_PRODUCT;
import static com.crud.util.AppConstants.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;
    private final ProductServiceJDBC productServiceJDBC;

    // JDBC ...


    @GetMapping("pagination-jdbc")
    public RestResponse<PageableResponse<ProductResponse>> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "productName", required = false) String productName) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY READED",
                productServiceJDBC.getAllPaginationProducts(pageNumber, pageSize, productName));
    }



    @GetMapping(value = "/jdbc", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<List<ProductResponse>> getAllProductJDBC() {
        return new  RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY READED",
                productServiceJDBC.getAllProductJDBC());
    }

    @PostMapping(value = "/jdbc", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ProductResponse> createProductJDBC(@RequestBody ProductRequest productRequest){
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.CREATED),
                "PRODUCT SUCCESSFULLY CREATED",
                productServiceJDBC.createProduct(productRequest));
    }

    @PutMapping(value = "/jdbc/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ProductResponse> updatedProductJDBC(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY UPDATED",
                productServiceJDBC.updatedProduct(id, productRequest));
    }

    @GetMapping(value = "/jdbc/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ProductResponse> getProductByIdJDBC(@PathVariable Long id) {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY READED",
                productServiceJDBC.getByIdProductJDBC(id));
    }

    @DeleteMapping("/jdbc/{id}")
    public RestResponse<String> deleteProductJDBC(@PathVariable Long id) {
        productServiceJDBC.deleteByIdProductJDBC(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY DELETED",
                "null"); // Data null.
    }

    // HIBERNATE ...

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public  RestResponse<PageableResponse<ProductResponse>> pageableProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new  RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY READED",
                productService.pageableProducts(numeroDePagina, medidaDePagina, ordenarPor, sortDir));

    }

/*    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.CREATED),
                "PRODUCT SUCCESSFULLY CREATED",
                productService.createProduct(productRequest));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ProductResponse> updatedProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY UPDATED",
                productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public RestResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY DELETED",
                "null"); // Data null.
    }*/

    @GetMapping("/export-excel")
    public ResponseEntity<Resource> getExportDataExcel(@RequestParam(value = "pageNo", defaultValue = AppConstants.NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
                                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
                                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
                                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir,
                                                       @RequestParam(defaultValue = AppConstants.FORMATO_EXCEL_ABREVIATURA) @NotBlank String format) throws Exception {
        PageableResponse<ProductResponse> productPage = productService.pageableProducts(numeroDePagina, medidaDePagina, ordenarPor, sortDir);
        List<ProductResponse> products = productPage.getContent();
        File file = productService.exportDataExcel(products, format);

        // Configurar las cabeceras de la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        // Crear la respuesta HTTP con el objeto File
        FileSystemResource fileResource = new FileSystemResource(file);
        return new ResponseEntity<>(fileResource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/create-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResponse<ProductResponse> createProductImage(
            @ApiParam(value = "Image file")
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId
    ) throws IOException {
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.CREATED),
                "PRODUCT SUCCESSFULLY CREATED",
                productService.createProductImage(image, name, price, description, categoryId));
    }

    @PutMapping(value = "/update-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResponse<ProductResponse> updatedProductImage(
            @PathVariable Long id,
            @ApiParam(value = "Image file")
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId
    ) throws IOException {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY UPDATED",
                productService.updatedProductImage(id,image, name, price, description, categoryId));
    }

    @GetMapping("/upload-img/{id}")
    public ResponseEntity<Resource> viewImage(@PathVariable Long id){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(productService.getProductImage(id));
    }
}








