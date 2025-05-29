package org.codenova.craft.repository;

import org.codenova.craft.entity.Order;
import org.codenova.craft.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
    // List<OrderItem> findByOrderId(Long orderId);
}
