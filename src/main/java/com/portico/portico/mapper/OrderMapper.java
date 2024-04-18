package com.portico.portico.mapper;

import com.portico.portico.domain.Order;
import com.portico.portico.domain.OrderContents;
import com.portico.portico.web.schema.OrderContentsSchema;
import com.portico.portico.web.schema.OrderSchema;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order mapToOrder(OrderSchema orderSchema) {
        Order order = new Order();
        order.setCarrierId(orderSchema.getCarrierId());
        order.setCustomerId(orderSchema.getCustomerId());
        order.setTotalCost(orderSchema.getTotalCost());
        order.setStatus(orderSchema.getStatus());
        order.setOrderDate(orderSchema.getOrderDate());
        order.setDeliveryDate(orderSchema.getDeliveryDate());
        order.setDeliveryAddress(orderSchema.getDeliveryAddress());

        // Map the list of orderContents from the orderSchema to the order object
        List<OrderContents> orderContentsList = orderSchema.getOrderContents().stream()
                .map(schema -> OrderMapper.toOrderContents(schema))
                .collect(Collectors.toList());

        order.setOrderContents(orderContentsList);

        return order;
    }

    public static OrderContents toOrderContents(OrderContentsSchema orderContentsSchema) {
        OrderContents orderContents = new OrderContents();
        orderContents.setProductId(orderContentsSchema.getProductId());
        orderContents.setWarehouseId(orderContentsSchema.getWarehouseId());
        orderContents.setQuantity(orderContentsSchema.getQuantity());
        return orderContents;
    }
}

