package com.crud.config.hateoas;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import com.crud.controller.CategoryController;
import com.crud.controller.ProductController;
import com.crud.dtos.response.CategoryResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CategoryHateoasConfig implements RepresentationModelAssembler<CategoryResponse, EntityModel<CategoryResponse>> {

    @Override
    public EntityModel<CategoryResponse> toModel(CategoryResponse entity) {
        EntityModel<CategoryResponse> categoryModel = EntityModel.of(entity);

        categoryModel.add(linkTo(methodOn(CategoryController.class).getCategoryById(entity.getId())).withSelfRel());
        categoryModel.add(linkTo(methodOn(CategoryController.class).createCategory(null)).withRel("create"));
        categoryModel.add(linkTo(methodOn(CategoryController.class).listCategories()).withRel(IanaLinkRelations.COLLECTION));

        return categoryModel;
    }
}
