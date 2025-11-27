package com.bookshop.delivery_service.delivery.event;

import com.bookshop.delivery_service.delivery.domain.DeliveryStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DeliveryEvent {
    Long id;
    Long orderId;

    String userId;
    String firstName;
    String lastName;
    String email;
    String phone;

    String courierFirstName;
    String courierLastName;
    String courierId;
    String courierEmail;
    String courierPhone;

    DeliveryStatus status;
    Boolean exchange;
    BigDecimal amount;

    String shippingStreet;
    String shippingCity;
    String shippingState;
    String shippingPostalCode;
    String shippingCountry;

    Instant createdDate;
    Instant lastModifiedDate;
}
