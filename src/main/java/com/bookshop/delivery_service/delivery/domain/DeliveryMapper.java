package com.bookshop.delivery_service.delivery.domain;

import com.bookshop.delivery_service.delivery.event.DeliveryEvent;
import com.bookshop.delivery_service.delivery.event.OrderEvent;
import com.bookshop.delivery_service.delivery.web.DeliveryUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeliveryMapper {
    void updateDelivery(@MappingTarget Delivery delivery, DeliveryUpdateRequest request);
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "id", target = "orderId")
    @Mapping(target = "id", ignore = true)
    Delivery toDelivery(OrderEvent event);

    DeliveryEvent toDeliveryEvent(Delivery delivery);
}
