package com.example.shop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shop.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByPersonId(int personId);
}
