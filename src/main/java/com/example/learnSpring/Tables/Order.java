package com.example.learnSpring.Tables;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String orderName;
    private OrderStatus orderStatus;

    Order(){}

    Order(String orderName, OrderStatus orderStatus){
        this.orderName = orderName;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId(){
        return orderId;
    }

    public String getOrderName(){
        return this.orderName;
    }

    public OrderStatus getOrderStatus(){
        return this.orderStatus;
    }

    public void changeOrderState(OrderStatus newState){
        this.orderStatus = newState;
    }

    @Override
    public String toString(){
        return "Order id = " + this.orderId +", Order name = "
                + this.orderName + ", Order status = " + this.orderStatus;
    }

}
