package com.bookshop.delivery_service.delivery.domain;

public class InvalidDeliveryStateException extends RuntimeException {
    public InvalidDeliveryStateException(String state) {
        super("Cannot process delivery in state: " + state);
    }
}
