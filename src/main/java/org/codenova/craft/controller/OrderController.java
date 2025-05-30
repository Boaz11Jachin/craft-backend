package org.codenova.craft.controller;


import lombok.RequiredArgsConstructor;
import org.codenova.craft.entity.Inventory;
import org.codenova.craft.entity.Order;
import org.codenova.craft.entity.OrderItem;
import org.codenova.craft.entity.Product;
import org.codenova.craft.repository.InventoryRepository;
import org.codenova.craft.repository.OrderItemRepository;
import org.codenova.craft.repository.OrderRepository;
import org.codenova.craft.repository.ProductRepository;
import org.codenova.craft.response.Demand;
import org.codenova.craft.response.OrderItemSummary;
import org.codenova.craft.resquest.NewOrder;
import org.codenova.craft.service.BomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final InventoryRepository inventoryRepository;

    private final BomService bomService;

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

    @GetMapping("/api/order")
    public ResponseEntity<?> getAllOrders( ) {
        List<Order> orders = orderRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("orders", orders);
        return ResponseEntity.status(200).body(response);
    }


    @GetMapping("/api/order/{orderId}")
    public ResponseEntity<?> getAllOrders(@PathVariable Long orderId) {
        // findById --> Optional 로 나오니까 이거 분기처리 해야된다.
        Order order =orderRepository.findById(orderId).orElseThrow();
        List<OrderItem> items = orderItemRepository.findByOrder(order);
        List<OrderItemSummary> itemsSummary = items.stream().map((elm)-> {
            return OrderItemSummary.builder()
                    .id(elm.getId()).productId(elm.getProduct().getId())
                    .productName(elm.getProduct().getName())
                    .quantity(elm.getQuantity())
                    .status(elm.getStatus()).build();
        }).toList();



        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("order", order);
        response.put("orderItems", itemsSummary);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/api/order/{orderId}/approve")
    public ResponseEntity<?> approveOrder(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        List<OrderItem> orderItemList = orderItemRepository.findByOrder(order);

        for(OrderItem orderItem : orderItemList) {
            Inventory inventory = inventoryRepository.findByProduct(orderItem.getProduct());
            if(inventory.getStockQuantity() - inventory.getReservedQuantity() > orderItem.getQuantity()) {
                orderItem.setStatus("COMPLETED");
                orderItemRepository.save(orderItem);
                inventory.setReservedQuantity(inventory.getReservedQuantity() + orderItem.getQuantity());
                inventoryRepository.save(inventory);
            }else {
                // -----------------------------------------
                List<Demand> demands =bomService.calculateRequiredMaterials(orderItem.getProduct());






            }

        }


        return null;
    }

}
