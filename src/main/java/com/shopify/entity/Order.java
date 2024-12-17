package com.shopify.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    private Map<Long, Integer> products;

    public enum OrderStatus {
        PENDING,
        PAID
    }
}
