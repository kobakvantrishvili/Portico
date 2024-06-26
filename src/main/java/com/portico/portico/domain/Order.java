package com.portico.portico.domain;

import com.portico.portico.domain.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Order {
    private Integer id;
    private Integer carrierId;
    private Integer customerId;
    private BigDecimal totalCost;
    private OrderStatus status;
    private Date orderDate;
    private Date deliveryDate;
    private String deliveryAddress;
    private List<OrderContent> orderContents;
}
