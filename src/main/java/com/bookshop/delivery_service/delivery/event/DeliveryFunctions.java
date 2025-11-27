package com.bookshop.delivery_service.delivery.event;

import com.bookshop.delivery_service.delivery.domain.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class DeliveryFunctions {

	@Bean
	public Consumer<Flux<OrderEvent>> handleOrderEvent(DeliveryService deliveryService) {
		return flux -> deliveryService.consumeOrderEvent(flux)
				.doOnNext(delivery -> log.info("Handle order event with order: {}", delivery.getOrderId()))
				.subscribe();
	}

    @Bean
    public Consumer<Flux<PaymentEvent>> handlePaymentEvent(DeliveryService deliveryService) {
        return flux -> deliveryService.consumePaymentEvent(flux)
                .doOnNext(delivery -> log.info("Handle payment event with order: {}", delivery.getOrderId()))
                .subscribe();
    }
}
