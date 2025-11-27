package com.bookshop.delivery_service.delivery.domain;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String status) {
        super("Invalid order status for delivery processing: " + status);
    }
}