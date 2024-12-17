package com.shopify.repository;

import com.shopify.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM com.shopify.entity.Order o WHERE o.status != 'PAID' AND o.createdDate < :timeThreshold")
    List<Order> findUnpaidOrdersOlderThan(Instant timeThreshold);

}
