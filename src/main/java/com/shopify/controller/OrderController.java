package com.shopify.controller;

import com.shopify.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.shopify.properties.Path.*;

@RestController
@RequestMapping(API_PATH_PREFIX + ORDERS)
@RequiredArgsConstructor
class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Map<Long, Integer> productsIdAndQuantity) {
        orderService.createOrder(productsIdAndQuantity);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(ORDER_ID + PAY)
    public ResponseEntity<Void> payOrder(@PathVariable Long orderId) {
        orderService.payOrder(orderId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
