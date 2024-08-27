package com.techie.microservices.order_service.service;

import com.techie.microservices.order_service.client.InventoryClient;
import com.techie.microservices.order_service.dto.OrderRequest;
import com.techie.microservices.order_service.event.OrderPlacedEvent;
import com.techie.microservices.order_service.model.Order;
import com.techie.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;


    public void createOrder(OrderRequest orderRequest) {
        var isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            //send the message to kafka topic
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
//            orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
//            orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            log.info("Start - Sending OrderPlacedEvent - {} to Kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent - {} to Kafka topic order-placed", orderPlacedEvent);
        } else {
            throw new IllegalArgumentException("Product with sku code " + orderRequest.skuCode() + " is not available in stock");

        }
    }
}
