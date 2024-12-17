package com.shopify.job;

import com.shopify.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class OrderScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "${scheduler.remove-expired-orders.cron}")
    public void removeExpiredOrders() {
        orderService.removeUnpaidOrders();
    }
}
