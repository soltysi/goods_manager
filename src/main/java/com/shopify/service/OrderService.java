package com.shopify.service;

import com.shopify.entity.Order;
import com.shopify.entity.Product;
import com.shopify.entity.User;
import com.shopify.exception.NotFoundException;
import com.shopify.repository.OrderRepository;
import com.shopify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public void createOrder(Map<Long, Integer> productIdAndQuantity) {
        User user = userService.getCurrentUser();

        List<Long> productIds = new ArrayList<>(productIdAndQuantity.keySet());
        List<Product> products = productRepository.findAllByIdIn(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<Long> notExistProducts = new ArrayList<>();
        List<Long> unavailableQuantity = new ArrayList<>();
        Map<Long, Integer> orderProducts = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : productIdAndQuantity.entrySet()) {
            Long productId = entry.getKey();
            Integer requestedQuantity = entry.getValue();

            Product product = productMap.get(productId);

            if (product == null) {
                notExistProducts.add(productId);
            } else if (product.getQuantity() < requestedQuantity) {
                unavailableQuantity.add(productId);
            } else {
                orderProducts.put(productId, requestedQuantity);
                product.setQuantity(product.getQuantity() - requestedQuantity);
            }
        }

        if (!notExistProducts.isEmpty()) {
            throw new NotFoundException("The following products are missing: " + notExistProducts);
        }

        if (!unavailableQuantity.isEmpty()) {
            throw new NotFoundException("Insufficient quantity of products: " + unavailableQuantity);
        }

        Order order = Order.builder()
                .status(Order.OrderStatus.PENDING)
                .user(user)
                .products(orderProducts)
                .build();

        orderRepository.save(order);
        productRepository.saveAll(productMap.values());
    }

    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NotFoundException::new);

        if (order.getStatus().equals(Order.OrderStatus.PAID)) {
            throw new IllegalArgumentException("Order already paid");
        }

        order.setStatus(Order.OrderStatus.PAID);
        orderRepository.save(order);
    }

    public void removeUnpaidOrders() {
        Instant timeThreshold = Instant.now().minusSeconds(10 * 60);

        List<Order> unpaidOrders = orderRepository.findUnpaidOrdersOlderThan(timeThreshold);

        if (!unpaidOrders.isEmpty()) {
            orderRepository.deleteAll(unpaidOrders);
            System.out.println("Removed " + unpaidOrders.size() + " unpaid orders older than 10 minutes.");
        }
    }
}
