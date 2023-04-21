package com.example.shop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.repositories.OrderStatusRepository;

@Service
@Transactional(readOnly = true)
public class OrderStatusService {
    

    private OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }
}
