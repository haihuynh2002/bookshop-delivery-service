package com.bookshop.delivery_service.delivery.domain;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(Long deliveryId) {
        super("Delivery not found with id: " + deliveryId);
    }

    public DeliveryNotFoundException(String orderId) {
        super("Delivery not found for order id: " + orderId);
    }
}
