package com.Kilari.repository;

import com.Kilari.modal.Order;
import com.Kilari.modal.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
