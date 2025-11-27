package com.bookshop.delivery_service.delivery.web;

import com.bookshop.delivery_service.delivery.domain.DeliveryNotFoundException;
import com.bookshop.delivery_service.delivery.domain.InvalidOrderStatusException;
import com.bookshop.delivery_service.delivery.domain.InvalidPaymentStatusException;
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

    @ExceptionHandler(InvalidOrderStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleInvalidOrderStatusException(InvalidOrderStatusException ex) {
        return Mono.just(ErrorResponse.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(InvalidPaymentStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleInvalidPaymentStatusException(InvalidPaymentStatusException ex) {
        return Mono.just(ErrorResponse.builder()
                .message(ex.getMessage())
                .build());
    }
}