package com.example.learnSpring.Repositories;

import com.example.learnSpring.Tables.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderName(String orderName);
    List<Order> findByOrderNameContaining(String ordername);
}
