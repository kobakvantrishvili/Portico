package com.portico.portico.mapper;

import com.portico.portico.domain.Order;
import com.portico.portico.domain.OrderContent;
import com.portico.portico.web.schema.OrderContentsSchema;
import com.portico.portico.web.schema.OrderSchema;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order mapToOrder(OrderSchema orderSchema) {
        Order order = new Order();
        order.setCarrierId(orderSchema.getCarrierId());
        order.setCustomerId(orderSchema.getCustomerId());
        order.setStatus(orderSchema.getStatus());
        order.setOrderDate(orderSchema.getOrderDate());
        order.setDeliveryDate(orderSchema.getDeliveryDate());
        order.setDeliveryAddress(orderSchema.getDeliveryAddress());

        // Map the list of orderContents from the orderSchema to the order object
        List<OrderContent> orderContentsList = orderSchema.getOrderContents().stream()
                .map(schema -> OrderMapper.toOrderContents(schema))
                .collect(Collectors.toList());

        order.setOrderContents(orderContentsList);

        return order;
    }

    public static OrderContent toOrderContents(OrderContentsSchema orderContentsSchema) {
        OrderContent orderContents = new OrderContent();
        orderContents.setProductId(orderContentsSchema.getProductId());
        orderContents.setWarehouseId(orderContentsSchema.getWarehouseId());
        orderContents.setQuantity(orderContentsSchema.getQuantity());
        return orderContents;
    }
}

