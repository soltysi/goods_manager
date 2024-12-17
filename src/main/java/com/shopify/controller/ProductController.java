package com.shopify.controller;

import com.shopify.dto.CreateProductDto;
import com.shopify.dto.ProductDto;
import com.shopify.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shopify.properties.Path.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_PATH_PREFIX + PRODUCTS)
class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody @Valid CreateProductDto product) {
        productService.addProduct(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(@PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageRequest) {
        return new ResponseEntity<>(productService.findAllByPage(pageRequest), HttpStatus.OK);
    }

    @GetMapping(PRODUCT_ID)
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.findProductDtoById(productId), HttpStatus.OK);
    }
}
