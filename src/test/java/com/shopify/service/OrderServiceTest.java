package com.shopify.service;

import com.shopify.entity.Order;
import com.shopify.entity.Product;
import com.shopify.entity.User;
import com.shopify.exception.NotFoundException;
import com.shopify.repository.OrderRepository;
import com.shopify.repository.ProductRepository;
import com.shopify.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static com.shopify.entity.User.UserRole.MANAGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Створення замоканого користувача
        mockUser = new User("manager", "password", MANAGER);
    }

    @Test
    void testCreateOrder_Success() {
        // Підготовка даних
        Map<Long, Integer> productIdAndQuantity = Map.of(1L, 2);

        // Мокування продуктів
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setQuantity(10);
        when(productRepository.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(mockProduct));

        // Мокування поточного користувача
        when(userService.getCurrentUser()).thenReturn(mockUser);

        // Перевірка виконання без помилок
        assertDoesNotThrow(() -> orderService.createOrder(productIdAndQuantity));

        // Перевірка, чи були збережені замовлення
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).saveAll(anyCollection());
    }

    @Test
    void testCreateOrder_ThrowsNotFound_WhenProductNotExist() {
        // Підготовка даних
        Map<Long, Integer> productIdAndQuantity = Map.of(1L, 2);

        // Мокування відсутності продукту в базі
        when(productRepository.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());

        // Мокування поточного користувача
        when(userService.getCurrentUser()).thenReturn(mockUser);

        // Перевірка виключення NotFoundException
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> orderService.createOrder(productIdAndQuantity));
        assertTrue(thrown.getMessage().contains("The following products are missing"));
    }

    @Test
    void testCreateOrder_ThrowsNotFound_WhenInsufficientQuantity() {
        // Підготовка даних
        Map<Long, Integer> productIdAndQuantity = Map.of(1L, 5);

        // Мокування продукту з недостатньою кількістю
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setQuantity(2);
        when(productRepository.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(mockProduct));

        // Мокування поточного користувача
        when(userService.getCurrentUser()).thenReturn(mockUser);

        // Перевірка виключення NotFoundException
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> orderService.createOrder(productIdAndQuantity));
        assertTrue(thrown.getMessage().contains("Insufficient quantity of products"));
    }

    @Test
    void testPayOrder_Success() {
        // Створення замоканого замовлення
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setStatus(Order.OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        // Перевірка виконання без помилок
        assertDoesNotThrow(() -> orderService.payOrder(1L));

        // Перевірка зміни статусу на PAID
        assertEquals(Order.OrderStatus.PAID, mockOrder.getStatus());
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testPayOrder_ThrowsIllegalArgumentException_WhenAlreadyPaid() {
        // Створення замоканого замовлення з уже оплаченою позначкою
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setStatus(Order.OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        // Перевірка виключення IllegalArgumentException
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> orderService.payOrder(1L));
        assertTrue(thrown.getMessage().contains("Order already paid"));
    }

    @Test
    void testRemoveUnpaidOrders_Success() {
        // Мокування непідтверджених замовлень
        List<Order> unpaidOrders = Collections.singletonList(new Order());
        when(orderRepository.findUnpaidOrdersOlderThan(any())).thenReturn(unpaidOrders);

        // Перевірка виконання без помилок
        assertDoesNotThrow(() -> orderService.removeUnpaidOrders());

        // Перевірка виклику видалення замовлень
        verify(orderRepository, times(1)).deleteAll(unpaidOrders);
    }
}