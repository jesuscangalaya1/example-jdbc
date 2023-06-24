package com.crud.controller;

import java.util.List;


import com.crud.dtos.response.PageableResponse;
import com.crud.repositories.jdbc.CategoryRepositoryJDBC;
import com.crud.services.servicejdbc.CategoryServiceJDBC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crud.dtos.request.CategoryRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.RestResponse;
import com.crud.services.CategoryService;

import lombok.RequiredArgsConstructor;

import static com.crud.util.AppConstants.*;
import static com.crud.util.AppConstants.MESSAGE_ID_PRODUCT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

        private final CategoryService categoryService;
        private final CategoryServiceJDBC categoryServiceJDBC;
        // jdbc ..

        @GetMapping("pagination-jdbc")
        public RestResponse<PageableResponse<CategoryResponse>> getAllCategories(
                @RequestParam(value = "pageNo", defaultValue = "1") int pageNumber,
                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

                return new  RestResponse<>(SUCCESS,
                        String.valueOf(HttpStatus.OK),
                        "PRODUCT SUCCESSFULLY READED",
                        categoryServiceJDBC.getAllPaginationCategoryJDBC(pageNumber, pageSize));

        }



                @GetMapping(value = "/jdbc", produces = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<List<CategoryResponse>> getAllCategoryJDBC() {
                return new  RestResponse<>(SUCCESS,
                        String.valueOf(HttpStatus.OK),
                        "PRODUCT SUCCESSFULLY READED",
                        categoryServiceJDBC.getAllCategoryJDBC());
        }

        @PostMapping(value = "/jdbc", produces = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<CategoryResponse> createProductJDBC(@RequestBody CategoryRequest categoryRequest){
                return new RestResponse<>(SUCCESS,
                        String.valueOf(HttpStatus.CREATED),
                        "PRODUCT SUCCESSFULLY CREATED",
                        categoryServiceJDBC.createCategory(categoryRequest));
        }

        @PutMapping(value = "/jdbc/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<CategoryResponse> updatedProductJDBC(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
                return new RestResponse<>(SUCCESS,
                        String.valueOf(HttpStatus.OK),
                        MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY UPDATED",
                        categoryServiceJDBC.updatedCategory(id, categoryRequest));
        }

        @GetMapping(value = "/jdbc/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<CategoryResponse> getProductByIdJDBC(@PathVariable Long id) {
                return new RestResponse<>(SUCCESS,
                        String.valueOf(HttpStatus.OK),
                        MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY READED",
                        categoryServiceJDBC.getByIdCategoryJDBC(id));
        }


        @DeleteMapping("/jdbc/{id}")
        public RestResponse<String> deleteProductJDBC(@PathVariable Long id) {
                categoryServiceJDBC.deleteByIdCategoryJDBC(id);
                return new RestResponse<>(SUCCESS,
                        String.valueOf(HttpStatus.OK),
                        MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY DELETED",
                        "null"); // Data null.
        }





        // hibernate ..
        /*@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<CategoryResponse>> listCategories() {

                return new ResponseEntity<>(categoryService.listCategories(), HttpStatus.OK);
        }

        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
                return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
        }

        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<CategoryResponse> createCategory(
                        @RequestBody CategoryRequest categoryRequest) {
                return new RestResponse<>(SUCCESS,
                                String.valueOf(HttpStatus.CREATED),
                                "CATEGORY SUCCESSFULLY CREATED",
                                categoryService.createCategory(categoryRequest));
        }

        @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<CategoryResponse> updatedCategory(@PathVariable Long id,
                        @RequestBody CategoryRequest categoryRequest) {
                return new RestResponse<>(SUCCESS,
                                String.valueOf(HttpStatus.OK),
                                MESSAGE_ID_CATEGORY + id + " SUCCESSFULLY UPDATED",
                                categoryService.updateCategory(id, categoryRequest));
        }

        @DeleteMapping("/{id}")
        public RestResponse<String> deleteCategory(@PathVariable Long id) {
                categoryService.deleteCategory(id);
                return new RestResponse<>(SUCCESS,
                                String.valueOf(HttpStatus.OK),
                                MESSAGE_ID_CATEGORY + id + " SUCCESSFULLY DELETED",
                                "null"); // Data null.
        }

*/

}
