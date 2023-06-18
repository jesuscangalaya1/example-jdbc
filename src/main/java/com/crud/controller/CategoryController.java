package com.crud.controller;

import java.util.List;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.ProductResponse;
import com.crud.services.servicejdbc.CategoryServiceJDBC;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.config.hateoas.CategoryHateoasConfig;
import com.crud.dtos.request.CategoryRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.RestResponse;
import com.crud.services.CategoryService;
import com.crud.util.AppConstants;

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
        private final CategoryHateoasConfig categoryHateoasConfig;

        // jdbc ..

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
        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<CategoryResponse>> listCategories() {

                return new ResponseEntity<>(categoryService.listCategories(), HttpStatus.OK);
        }

        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
                return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
        }

        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<EntityModel<CategoryResponse>> createCategory(
                        @RequestBody CategoryRequest categoryRequest) {
                return new RestResponse<>(SUCCESS,
                                String.valueOf(HttpStatus.CREATED),
                                "CATEGORY SUCCESSFULLY CREATED",
                                categoryHateoasConfig.toModel(categoryService.createCategory(categoryRequest)));
        }

        @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
        public RestResponse<EntityModel<CategoryResponse>> updatedCategory(@PathVariable Long id,
                        @RequestBody CategoryRequest categoryRequest) {
                return new RestResponse<>(SUCCESS,
                                String.valueOf(HttpStatus.OK),
                                MESSAGE_ID_CATEGORY + id + " SUCCESSFULLY UPDATED",
                                categoryHateoasConfig.toModel(categoryService.updateCategory(id, categoryRequest)));
        }

        @DeleteMapping("/{id}")
        public RestResponse<String> deleteCategory(@PathVariable Long id) {
                categoryService.deleteCategory(id);
                return new RestResponse<>(SUCCESS,
                                String.valueOf(HttpStatus.OK),
                                MESSAGE_ID_CATEGORY + id + " SUCCESSFULLY DELETED",
                                "null"); // Data null.
        }



}
