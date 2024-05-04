package com.portico.portico.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PAYMENT_PROCESSING(0, "Payment Processing"),
    PACKAGING(1, "Packaging"),
    SHIPPED(2, "Shipped"),
    OUT_FOR_DELIVERY(3, "Out for Delivery"),
    DELIVERED(4, "Delivered"),
    CANCELLED(5, "Cancelled");

    private final int value;
    private final String displayValue;

    OrderStatus(int value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.displayValue.equalsIgnoreCase(status)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus: " + status);
    }

}
