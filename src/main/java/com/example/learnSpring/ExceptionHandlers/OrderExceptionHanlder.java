package com.example.learnSpring.ExceptionHandlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OrderExceptionHanlder extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(OrderNotFoundByIdException.class)
//    protected ResponseEntity<?> handleOrderNotFoundById(
//            OrderNotFoundByIdException ex,
//            WebRequest request){
//        Map<String, Object> body = Collections.singletonMap("message", ex.getMessage());
//        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(OrderNotFoundByIdException.class)
    protected ResponseEntity<?> handleOrderNotFoundById(
            OrderNotFoundByIdException ex,
            WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ChangeOrderStatusException.class)
    protected ResponseEntity<?> handleChangeOrderStatusException(
            ChangeOrderStatusException ex,
            WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
