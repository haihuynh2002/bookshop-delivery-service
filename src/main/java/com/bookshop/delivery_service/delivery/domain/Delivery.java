package com.bookshop.delivery_service.delivery.domain;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "delivery")
public class Delivery {

    @Id
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be a positive number")
    Long id;

    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID must be a positive number")
    Long orderId;

    @NotBlank(message = "Customer ID cannot be blank")
    @Size(min = 1, max = 50, message = "Customer ID must be between 1 and 50 characters")
    String userId;

    String firstName;

    String lastName;

    BigDecimal amount;

    @Email(message = "Customer email must be a valid email address")
    @NotBlank(message = "Customer email cannot be blank")
    String email;

    @Pattern(regexp = "^\\+?[\\d\\s-()]{10,20}$", message = "Customer phone must be a valid phone number")
    @NotBlank(message = "Customer phone cannot be blank")
    String phone;

    @NotBlank(message = "Courier ID cannot be blank")
    @Size(min = 1, max = 50, message = "Courier ID must be between 1 and 50 characters")
    String courierId;

    String courierFirstName;

    String courierLastName;

    @Email(message = "Courier email must be a valid email address")
    @NotBlank(message = "Courier email cannot be blank")
    String courierEmail;

    @Pattern(regexp = "^\\+?[\\d\\s-()]{10,20}$", message = "Courier phone must be a valid phone number")
    @NotBlank(message = "Courier phone cannot be blank")
    String courierPhone;

    @NotNull(message = "Delivery status cannot be null")
    DeliveryStatus status;

    Boolean exchange;

    String shippingStreet;
    String shippingCity;
    String shippingState;
    String shippingPostalCode;
    String shippingCountry;

    @CreatedDate
    Instant createdDate;

    @LastModifiedDate
    Instant lastModifiedDate;

    @Version
    Long version;
}