package com.bookshop.delivery_service.delivery.web;

import com.bookshop.delivery_service.delivery.domain.DeliveryStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DeliveryUpdateRequest {
    DeliveryStatus status;
}
