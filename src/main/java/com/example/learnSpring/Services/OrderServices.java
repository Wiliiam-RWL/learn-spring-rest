package com.example.learnSpring.Services;

import com.example.learnSpring.ExceptionHandlers.ChangeOrderStatusException;
import com.example.learnSpring.ExceptionHandlers.OrderNotFoundByIdException;
import com.example.learnSpring.Repositories.OrderRepository;
import com.example.learnSpring.Tables.Order;
import com.example.learnSpring.Tables.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServices {
    private final OrderRepository repo;

    @Autowired
    OrderServices(OrderRepository repo){
        this.repo = repo;
    }

    public List<Order> getAll(){
        return repo.findAll();
    }

    public Optional<Order> getById(Long id){
        return repo.findById(id);
    }

    public List<Order> getByName(String orderName){
        return repo.findByOrderNameContaining(orderName);
    }

    public Order addOrder(Order order){
        order.changeOrderState(OrderStatus.PROCESSING);
        return repo.save(order);
    }

    public Order changeOrderStatus(Long id, OrderStatus newStatus){
        Order order = getById(id).orElseThrow(()->new OrderNotFoundByIdException(id));
        if(order.getOrderStatus()==OrderStatus.CANCELED || order.getOrderStatus()==OrderStatus.FINISHED){
            throw new ChangeOrderStatusException(order.getOrderStatus());
        }
        else{
            order.changeOrderState(newStatus);
            return repo.save(order);
        }
    }
}
