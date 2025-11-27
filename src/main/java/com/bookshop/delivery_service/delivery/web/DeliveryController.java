package com.bookshop.delivery_service.delivery.web;

import com.bookshop.delivery_service.delivery.domain.Delivery;
import com.bookshop.delivery_service.delivery.domain.DeliveryService;
import com.bookshop.delivery_service.delivery.domain.DeliveryStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("deliveries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryController {
	DeliveryService deliveryService;

    @GetMapping
	public Flux<Delivery> getAllDeliveries() {
		return deliveryService.getAllDeliveries();
	}

    @GetMapping("/my-deliveries")
    public Flux<Delivery> getDeliveriesByCourier(@AuthenticationPrincipal Jwt jwt) {
        return deliveryService.getDeliveriesByCourierId(jwt.getSubject());
    }

	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<Delivery> updateDelivery(
            @PathVariable Long id,
            @RequestBody DeliveryUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
		return deliveryService.updateDelivery(id, request, jwt);
	}



}
