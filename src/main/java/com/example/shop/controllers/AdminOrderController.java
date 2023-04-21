package com.example.shop.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.models.Order;
import com.example.shop.services.OrderService;

@Controller
public class AdminOrderController {
    
    private OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String index(@RequestParam(value = "query", required = false, defaultValue = "") String query, Model model) {

        if (query.isEmpty()) {
            List<Order> orderList = orderService.getAllOrder();
            model.addAttribute("orderList", orderList);
        } else {
            List<Order> orderList = orderService.getByNumberLastPartString(query.toLowerCase());
            model.addAttribute("orderList", orderList);
        }

        model.addAttribute("title", "Список заказов");
        return "admin/order/list";
    }
}
