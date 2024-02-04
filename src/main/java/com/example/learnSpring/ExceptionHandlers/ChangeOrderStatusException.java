package com.example.learnSpring.ExceptionHandlers;

import com.example.learnSpring.Tables.OrderStatus;

public class ChangeOrderStatusException extends RuntimeException{
    public ChangeOrderStatusException(OrderStatus status){
        super("Cannot change a " + status + "order");
    }
}
