package com.example.learnSpring.ExceptionHandlers;

public class OrderNotFoundByIdException extends RuntimeException{
    public OrderNotFoundByIdException(Long id){
        super("Could not find order id: "+id);
    }
}
