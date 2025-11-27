package com.bookshop.delivery_service.delivery.web;

import com.bookshop.delivery_service.delivery.domain.DeliveryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class DeliveryControllerAdvice {

    @ExceptionHandler(DeliveryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleDeliveryNotFoundException(DeliveryNotFoundException ex) {
        return Mono.just(ErrorResponse.builder()
                .message(ex.getMessage())
                .build());
    }
}