package com.example.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.models.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    
}
