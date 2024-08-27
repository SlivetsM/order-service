package com.techie.microservices.order_service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.techie.microservices.order_service.model.Order}
 */
public record ResponseOrder(Long id, String orderNumber, String scuCode, BigDecimal price,
                            Integer quantity) implements Serializable {
}