package com.shopify.service;

import com.shopify.dto.CreateProductDto;
import com.shopify.dto.ProductDto;
import com.shopify.entity.Product;
import com.shopify.exception.NotFoundException;
import com.shopify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(CreateProductDto productDto) {
        productRepository.save(Product.of(productDto));
    }

    public Page<ProductDto> findAllByPage(Pageable pageable) {
        return productRepository.findAllByPage(pageable);
    }

    public ProductDto findProductDtoById(Long productId) {
        return productRepository.findProductDtoById(productId)
                .orElseThrow(NotFoundException::new);
    }
}
