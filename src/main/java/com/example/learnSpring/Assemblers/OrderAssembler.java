package com.example.learnSpring.Assemblers;

import com.example.learnSpring.Controllers.OrderController;
import com.example.learnSpring.Tables.Order;
import com.example.learnSpring.Tables.OrderStatus;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class OrderAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order){
        EntityModel<Order> model = EntityModel.of(order, //
                linkTo(methodOn(OrderController.class).detail(order.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("all_orders"));
        if(order.getOrderStatus() == OrderStatus.PROCESSING){
            model.add(linkTo(methodOn(OrderController.class).complete(order.getOrderId())).withRel("complete"));
            model.add(linkTo(methodOn(OrderController.class).cancel(order.getOrderId())).withRel("cancel"));
        }
        return model;
    }
}
