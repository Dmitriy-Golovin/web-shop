package com.example.shop.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.models.OrderStatus;
import com.example.shop.repositories.OrderStatusRepository;

@Service
@Transactional(readOnly = true)
public class OrderStatusService {
    

    private OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public OrderStatus getLastStatusById(int orderId) {
        Optional<OrderStatus> optionalStatus = orderStatusRepository.findByOrderIdLastItem(orderId);

        return optionalStatus.orElse(null);
    }

    @Transactional
    public void saveOrderStatus(OrderStatus orderStatus) {
        orderStatusRepository.save(orderStatus);
    }
}
