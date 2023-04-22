package com.example.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.models.Order;
import com.example.shop.repositories.OrderRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public Order getOrderId(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    public List<Order> getByNumberLastPartString(String query) {
        return orderRepository.findByNumberLastPartLikeString(query);
    }

    public List<Order> getByPersonId(int personId) {
        return orderRepository.findByPersonId(personId);
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
