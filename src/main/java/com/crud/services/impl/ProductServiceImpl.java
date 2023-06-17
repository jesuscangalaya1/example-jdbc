package com.crud.services.impl;

import com.crud.dtos.request.ProductRequest;
import com.crud.dtos.response.CategoryResponse;
import com.crud.dtos.response.PageableResponse;
import com.crud.dtos.response.ProductResponse;
import com.crud.entities.CategoryEntity;
import com.crud.entities.ProductEntity;
import com.crud.exceptions.BusinessException;
import com.crud.mapper.ProductMapper;
import com.crud.reports.export.ResourceExport;
import com.crud.repositories.CategoryRepository;
import com.crud.repositories.ProductRepository;
import com.crud.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.crud.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ResourceExport resourceExport;

    //Image
    @Override
    @CacheEvict(value = "Producto", allEntries = true)
    @Transactional
    public ProductResponse createProductImage(MultipartFile image, String name, Double price,
                                              String description, Long categoryId) throws IOException {

        byte[] imageBytes = extractImageBytes(image);


        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(name);
        productRequest.setPrice(price);
        productRequest.setDescription(description);
        productRequest.setCategoryId(categoryId);

        ProductEntity productEntity = productMapper.toEntity(productRequest);

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_CATEGORY + categoryId));
        productEntity.setCategory(categoryEntity);

        setProductImage(productEntity, imageBytes);

        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productMapper.toDto(savedProductEntity);
    }

    private byte[] extractImageBytes(MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            // Verificar si se proporcionó una imagen y no está vacía
            if (!image.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Invalid image file format");
            }

            return image.getBytes();
        }

        return null;
    }



    @Override
    @CacheEvict(value = "Producto", allEntries = true)
    @Transactional
    public ProductResponse updatedProductImage(Long id, MultipartFile image, String name, Double price,
                                               String description, Long categoryId) throws IOException {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_PRODUCT + id));

        byte[] imageBytes = extractImageBytes(image);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(name);
        productRequest.setPrice(price);
        productRequest.setDescription(description);
        productRequest.setCategoryId(categoryId);

        productMapper.updateProductFromDto(productRequest, productEntity);
        setProductImage(productEntity, imageBytes);

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_CATEGORY + categoryId));
        productEntity.setCategory(categoryEntity);

        return productMapper.toDto(productRepository.save(productEntity));
    }

    private void setProductImage(ProductEntity productEntity, byte[] imageBytes) {
        if (imageBytes != null) {
            productEntity.setImage(imageBytes);
        } else {
            productEntity.setImage(null);
        }
    }



    //Image
    @Override
    public Resource getProductImage(Long id) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
        if (optionalProductEntity.isEmpty() || optionalProductEntity.get().getImage() == null) {
            throw new BusinessException("Image no encontrada con el ID: " + id, HttpStatus.NOT_FOUND, "Image not found");
        }

        byte[] imageBytes = optionalProductEntity.get().getImage();
        return new ByteArrayResource(imageBytes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> listProducts() {
        List<ProductEntity> productoEntities = productRepository.findAll();
        return Optional.of(productoEntities)
                .filter(list -> !list.isEmpty())
                .map(productMapper::productsToProductDtos)
                .orElseThrow(() -> new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos"));
    }

    @Override
    @Cacheable(value = "Producto")
    @Transactional(readOnly = true)
    public PageableResponse<ProductResponse> pageableProducts(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina, medidaDePagina, sort);

        Page<ProductEntity> products = productRepository.findAll(pageable);

        List<ProductEntity> listProducts = products.getContent();
        List<ProductResponse> contenido = listProducts.stream().map(productMapper::toDto).toList();

        if (contenido.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos");
        }

        PageableResponse<ProductResponse> pageProductResponse = new PageableResponse<>();
        pageProductResponse.setContent(contenido);
        pageProductResponse.setPageNumber(products.getNumber());
        pageProductResponse.setPageSize(products.getSize());
        pageProductResponse.setTotalElements(products.getTotalElements());
        pageProductResponse.setTotalPages(products.getTotalPages());
        pageProductResponse.setLast(products.isLast());
        return pageProductResponse;
    }

    @Override
    @Cacheable(value = "Producto")
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_PRODUCT + id));
        return productMapper.toDto(entity);
    }

/*  // SE PUEDE CONFIGURAR CUANDO ESTA RELACIONADO EN UNA SOLA LOGICA.
    @Caching(evict = {
			@CacheEvict(value = "Producto", allEntries = true),
			@CacheEvict(value = "Categoria", allEntries = true)
	})

* */

    //Crear sin image
    @Override
    @CacheEvict(value = "Producto", allEntries = true)
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        CategoryEntity categoriaEntity = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_PRODUCT + productRequest.getCategoryId()));
        ProductEntity productoEntity = productMapper.toEntity(productRequest);
        productoEntity.setCategory(categoriaEntity);
        ProductEntity savedProductEntity = productRepository.save(productoEntity);
        return productMapper.toDto(savedProductEntity);
    }

    //Actualizar sin image
    @Override
    @CacheEvict(value = "Producto", allEntries = true)
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_PRODUCT + id));

        // Verificar si la categoríaId es válida
        Long categoriaId = productRequest.getCategoryId();
        if (categoriaId != null) {
            CategoryEntity categoriaEntity = categoryRepository.findById(categoriaId)
                    .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_CATEGORY + categoriaId));
            productEntity.setCategory(categoriaEntity);
        }

        productMapper.updateProductFromDto(productRequest, productEntity);

        productEntity = productRepository.save(productEntity);
        return productMapper.toDto(productEntity);
    }

    @Override
    @CacheEvict(value = "Producto", allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.BAD_REQUEST, BAD_REQUEST_PRODUCT + id);
        }
        productRepository.deleteById(id);
    }

    //Implementation Patron de diseño(SOLID)
/*   Principio de responsabilidad única (SRP): El método exportDataExcel se encarga únicamente de orquestar la exportación de los datos,
     mientras que la lógica relacionada con la validación de formato,
     creación de columnas y valores, se ha separado en métodos auxiliares con responsabilidades más específicas.
    */
    @Override
    public File exportDataExcel(List<ProductResponse> productResponses, String formato) throws Exception {
        validateFormato(formato);

        List<String> sheets = Collections.singletonList(SHEET_PRODUCT);

        Map<String, List<String>> colsBySheet = createColumnsBySheetMap();
        Map<String, List<Map<String, String>>> valuesBySheet = createValuesBySheetMap(productResponses);

        String reportName = REPORT_NAME_PRODUCT_PAGINABLE;
        if (formato.equals(FORMATO_EXCEL_ABREVIATURA)) {
            return resourceExport.generateExcel(sheets, colsBySheet, valuesBySheet, reportName);
        } else {
            return resourceExport.generatePdf(sheets, colsBySheet, valuesBySheet, reportName);
        }
    }

    //Validated Format
    private void validateFormato(String formato) {
        if (!ARRAY_FORMATO.contains(formato)) {
            throw new BusinessException(String.format("%s format not allowed", formato), HttpStatus.BAD_GATEWAY, "Bad");
        }
    }

    //Mapeando Columnas
    private Map<String, List<String>> createColumnsBySheetMap() {
        List<String> cols = Arrays.asList(
                COL_PRODUCT_ID,
                COL_PRODUCT_NAME,
                COL_PRODUCT_PRICE,
                COL_PRODUCT_DESCRIPTION,
                COL_CATEGORY_ID,
                COL_CATEGORY_NAME
        );

        Map<String, List<String>> colsBySheet = new HashMap<>();
        colsBySheet.put(SHEET_PRODUCT, cols);
        return colsBySheet;
    }

    private Map<String, List<Map<String, String>>> createValuesBySheetMap(List<ProductResponse> productResponses) {
        List<Map<String, String>> valoresHoja = new ArrayList<>();

        for (ProductResponse row : productResponses) {
            Map<String, String> valuesHojaRow = new HashMap<>();
            valuesHojaRow.put(COL_PRODUCT_ID, getStringValue(row.getId()));
            valuesHojaRow.put(COL_PRODUCT_NAME, getStringValue(row.getName()));
            valuesHojaRow.put(COL_PRODUCT_PRICE, getStringValue(row.getPrice()));
            valuesHojaRow.put(COL_PRODUCT_DESCRIPTION, getStringValue(row.getDescription()));
            CategoryResponse category = row.getCategory();
            valuesHojaRow.put(COL_CATEGORY_ID, category != null ? getStringValue(category.getId()) : "");
            valuesHojaRow.put(COL_CATEGORY_NAME, category != null ? getStringValue(category.getName()) : "");
            valoresHoja.add(valuesHojaRow);
        }

        Map<String, List<Map<String, String>>> valuesBySheet = new HashMap<>();
        valuesBySheet.put(SHEET_PRODUCT, valoresHoja);
        return valuesBySheet;
    }

    private String getStringValue(Object value) {
        return value != null ? value.toString() : VC_EMTY;
    }


}
