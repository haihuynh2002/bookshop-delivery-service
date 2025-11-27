package com.bookshop.delivery_service.delivery.domain;

import com.bookshop.delivery_service.delivery.event.*;
import com.bookshop.delivery_service.delivery.web.DeliveryUpdateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DeliveryService {
    
    DeliveryRepository deliveryRepository;
    DeliveryMapper deliveryMapper;
    StreamBridge streamBridge;

    public Flux<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Flux<Delivery> getDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status);
    }

    public Flux<Delivery> getDeliveriesByCourierId(String courierId) {
        return deliveryRepository.findByCourierId(courierId);
    }

    @Transactional
    public Mono<Delivery> updateDelivery(Long id, DeliveryUpdateRequest request, Jwt jwt) {
        return deliveryRepository.findById(id)
                .switchIfEmpty(Mono.error(new DeliveryNotFoundException(id)))
                .map(delivery -> updateDeliveryWithCourierInfo(delivery, request, jwt))
                .flatMap(deliveryRepository::save)
                .doOnNext(this::publishDeliveryEvent);
    }

    public Flux<Delivery> consumeOrderEvent(Flux<OrderEvent> flux) {
        return flux
                .flatMap(event -> switch (event.getStatus()) {
                    case OrderStatus.ACCEPTED -> event.getPaymentMethod().equals(PaymentMethod.CASH)
                            ? buildProcessingDelivery(event)
                            : buildPendingDelivery(event);
                    case OrderStatus.PAID -> buildPaidDelivery(event);
                    case OrderStatus.CANCELLED -> cancelDelivery(event.getId());
                    default -> Mono.error(new InvalidOrderStatusException(event.getStatus().toString()));
                })
                .flatMap(deliveryRepository::save);
    }

    public Flux<Delivery> consumePaymentEvent(Flux<PaymentEvent> flux) {
        return flux
                .flatMap(event -> switch (event.getStatus()) {
                    case PaymentStatus.COMPLETED -> processDelivery(event.getOrderId());
                    case PaymentStatus.CANCELLED -> cancelDelivery(event.getOrderId());
                    default -> Mono.error(new InvalidPaymentStatusException(event.getStatus().toString()));
                })
                .flatMap(deliveryRepository::save);
    }

    private Delivery updateDeliveryWithCourierInfo(Delivery delivery, DeliveryUpdateRequest request, Jwt jwt) {
        deliveryMapper.updateDelivery(delivery, request);
        delivery.setCourierId(jwt.getSubject());
        delivery.setCourierFirstName(jwt.getClaim(StandardClaimNames.GIVEN_NAME));
        delivery.setCourierLastName(jwt.getClaim(StandardClaimNames.FAMILY_NAME));
        delivery.setCourierEmail(jwt.getClaim(StandardClaimNames.EMAIL));
        delivery.setCourierPhone(jwt.getClaim(StandardClaimNames.PHONE_NUMBER));
        return delivery;
    }

    private void publishDeliveryEvent(Delivery delivery) {
        var deliveryEvent = deliveryMapper.toDeliveryEvent(delivery);
        log.info("Delivery event status: {}", deliveryEvent.getStatus());

        var result = streamBridge.send("delivery-out-0", deliveryEvent);
        log.info("Result of sending delivery with id {}: {}", delivery.getId(), result);
    }

    private Mono<Delivery> buildProcessingDelivery(OrderEvent event) {
        return Mono.fromCallable(() -> {
            var delivery = deliveryMapper.toDelivery(event);
            delivery.setStatus(DeliveryStatus.PROCESSING);
            return delivery;
        });
    }

    private Mono<Delivery> buildPaidDelivery(OrderEvent event) {
        return Mono.fromCallable(() -> {
            var delivery = deliveryMapper.toDelivery(event);
            delivery.setStatus(DeliveryStatus.PROCESSING);
            delivery.setAmount(BigDecimal.ZERO);
            return delivery;
        });
    }

    private Mono<Delivery> buildPendingDelivery(OrderEvent event) {
        return Mono.fromCallable(() -> {
            var delivery = deliveryMapper.toDelivery(event);
            delivery.setStatus(DeliveryStatus.PENDING);
            return delivery;
        });
    }

    private Mono<Delivery> processDelivery(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .switchIfEmpty(Mono.error(new DeliveryNotFoundException(orderId)))
                .flatMap(delivery -> {
                    if (!delivery.getStatus().equals(DeliveryStatus.PENDING)) {
                        return Mono.error(new InvalidDeliveryStateException(delivery.getStatus().toString()));
                    }
                    return Mono.just(markDeliveryAsProcessing(delivery));
                })
                .doOnNext(delivery -> log.info("Processing delivery for order: {}", orderId));
    }

    private Mono<Delivery> cancelDelivery(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .switchIfEmpty(Mono.empty())
                .map(this::markDeliveryAsCancelled)
                .doOnNext(delivery -> log.info("Cancelled delivery for order: {}", orderId));
    }

    private Delivery markDeliveryAsProcessing(Delivery delivery) {
        delivery.setAmount(BigDecimal.ZERO);
        delivery.setStatus(DeliveryStatus.PROCESSING);
        return delivery;
    }

    private Delivery markDeliveryAsCancelled(Delivery delivery) {
        delivery.setStatus(DeliveryStatus.CANCELLED);
        return delivery;
    }
}