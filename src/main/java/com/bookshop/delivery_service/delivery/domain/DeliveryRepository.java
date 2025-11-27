package com.bookshop.delivery_service.delivery.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryRepository extends ReactiveCrudRepository<Delivery,Long> {
    Mono<Delivery> findByOrderId(Long orderId);
    Flux<Delivery> findAllByOrderId(Long orderId);
    Flux<Delivery> findByStatus(DeliveryStatus status);
    Flux<Delivery>  findByCourierId(String id);
}
