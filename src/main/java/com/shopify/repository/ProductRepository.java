package com.shopify.repository;

import com.shopify.dto.ProductDto;
import com.shopify.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new com.shopify.dto.ProductDto(p.id, p.name, p.price, p.quantity) " +
            "FROM com.shopify.entity.Product p")
    Page<ProductDto> findAllByPage(Pageable pageable);


    @Query("SELECT new com.shopify.dto.ProductDto(p.id, p.name, p.price, p.quantity) " +
            "FROM com.shopify.entity.Product p " +
            "WHERE p.id = :productId")
    Optional<ProductDto> findProductDtoById(Long productId);

    List<Product> findAllByIdIn(List<Long> productIds);
}
