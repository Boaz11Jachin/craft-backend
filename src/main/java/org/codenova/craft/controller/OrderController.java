package org.codenova.craft.controller;


import lombok.RequiredArgsConstructor;
import org.codenova.craft.entity.Order;
import org.codenova.craft.entity.OrderItem;
import org.codenova.craft.repository.OrderItemRepository;
import org.codenova.craft.repository.OrderRepository;
import org.codenova.craft.repository.ProductRepository;
import org.codenova.craft.resquest.NewOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @PostMapping("/api/order/new")
    public ResponseEntity<?> newOrderHandle(@RequestBody NewOrder newOrder) {
//        System.out.println(newOrder.getDueDate());
//
//        for(NewOrder.Item o  : newOrder.getItems()) {
//            System.out.println(o.getProductId());
//            System.out.println(o.getQuantity());
//        }
//        // 검증용임. 필요없어짐

        Order order = Order.builder()
                .dueDate(newOrder.getDueDate())
                .build();
        orderRepository.save(order);

       /*
            for 문 혹은 stream
         */
        List<OrderItem> orderItems = new ArrayList<>();
        for (NewOrder.Item item : newOrder.getItems()) {
            String productId = item.getProductId();
            int quantity = item.getQuantity();

            OrderItem orderItem = OrderItem.builder().
                    order(order).
                    product(productRepository.findById(productId).get()).
                    quantity(quantity).
                    build();

            orderItems.add(orderItem);
        }


        orderItemRepository.saveAll(orderItems);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "successfully added order");
        response.put("order", order);
        return ResponseEntity.status(200).body(response);

    }


}
