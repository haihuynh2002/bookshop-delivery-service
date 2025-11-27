package com.bookshop.delivery_service.delivery.domain;

public class InvalidPaymentStatusException extends RuntimeException {
    public InvalidPaymentStatusException(String status) {
        super("Invalid payment status for delivery processing: " + status);
    }
}
