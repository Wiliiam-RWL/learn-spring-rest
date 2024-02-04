package com.example.learnSpring.Controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.learnSpring.Assemblers.OrderAssembler;
import com.example.learnSpring.ExceptionHandlers.ChangeOrderStatusException;
import com.example.learnSpring.ExceptionHandlers.OrderNotFoundByIdException;
import com.example.learnSpring.Services.OrderServices;
import com.example.learnSpring.Tables.Order;
import com.example.learnSpring.Tables.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderServices service;
    private final OrderAssembler assembler;

    @Autowired
    OrderController(OrderServices service, OrderAssembler assembler){
        this.service = service;
        this.assembler = assembler;
    }
    @GetMapping("")
    public RepresentationModel<?> greet() {
        RepresentationModel<?> model = new RepresentationModel<>();
        model.add(linkTo(methodOn(OrderController.class).all()).withRel("all_orders"));
        model.add(linkTo(methodOn(OrderController.class).search_name(null)).withRel("search_ny_name").expand());
        return model;
    }


    @GetMapping("/all")
    public CollectionModel<EntityModel<Order>> all(){
        List<EntityModel<Order>> allOrders = service.getAll().stream()
                .map(assembler::toModel).toList();
        return CollectionModel.of(allOrders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel(),
                linkTo(methodOn(OrderController.class).greet()).withRel("order_main_page"));
    }

    @GetMapping("/detail/{id}")
    public EntityModel<Order> detail(@PathVariable Long id){
        Order order = service.getById(id).orElseThrow(()-> new OrderNotFoundByIdException(id));
        return assembler.toModel(order);
    }

    @GetMapping("/search/name")
    public CollectionModel<EntityModel<Order>> search_name(
            @RequestParam(name = "orderName") String orderName
    ){
        List<Order> orders = service.getByName(orderName);
        // Missing Exception handler
        List<EntityModel<Order>> response = orders.stream().map(assembler::toModel).toList();
        return CollectionModel.of(response);
    }

    @PostMapping("/new")
    public ResponseEntity<EntityModel<Order>> add(@RequestBody Order order){
        Order newOrder = service.addOrder(order);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).detail(newOrder.getOrderId())).toUri())
                .body(assembler.toModel(newOrder));
    }

    @PatchMapping("/detail/{id}/status/finish")
    public ResponseEntity<Object> complete(@PathVariable Long id){
        Order order = service.changeOrderStatus(id, OrderStatus.FINISHED);
        return ResponseEntity.ok(assembler.toModel(order));
    }

    @PatchMapping("detail/{id}/status/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long id){
        Order order = service.changeOrderStatus(id, OrderStatus.CANCELED);
        return ResponseEntity.ok(assembler.toModel(order));
    }

}
