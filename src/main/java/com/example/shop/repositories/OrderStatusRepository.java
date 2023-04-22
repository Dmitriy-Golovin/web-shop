package com.example.shop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.models.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    
    @Query(value = "select * from order_status where order_id = ?1 order by id desc limit 1", nativeQuery = true)
    Optional<OrderStatus> findByOrderIdLastItem(int orderId);
}
