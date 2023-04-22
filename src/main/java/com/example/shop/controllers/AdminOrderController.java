package com.example.shop.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.example.shop.models.Order;
import com.example.shop.models.OrderStatus;
import com.example.shop.services.OrderService;
import com.example.shop.services.OrderStatusService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminOrderController {
    
    private OrderService orderService;

    private OrderStatusService orderStatusService;

    public AdminOrderController(OrderService orderService, OrderStatusService orderStatusService) {
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
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

    @GetMapping("/admin/order/details/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Order orderDetails = orderService.getOrderId(id);

        if (orderDetails == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Товар не найден"
            );
        }

        model.addAttribute("order", orderDetails);
        model.addAttribute("title", "Заказ №: " + orderDetails.getNumber());
        model.addAttribute("lastStatus", orderStatusService.getLastStatusById(orderDetails.getId()));
        return "admin/order/details";
    }

    @PostMapping("/admin/order/change-status")
    public String changeOrderStatus(@RequestParam("orderId") int orderId, @RequestParam("status") int status, HttpServletRequest request, RedirectAttributes attributes) {

        Order order = orderService.getOrderId(orderId);

        if (order == null) {
            attributes.addFlashAttribute("error", "Заказ не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus orderStatus = new OrderStatus(status, order);
        orderStatusService.saveOrderStatus(orderStatus);

        return "redirect:/admin/order/details/" + order.getId();
    }

    @PostMapping("/admin/order/set-checkout-status")
    public String setCheckoutStatus(@RequestParam("orderId") int orderId, HttpServletRequest request, RedirectAttributes attributes) {

        Order order = orderService.getOrderId(orderId);

        if (order == null) {
            attributes.addFlashAttribute("error", "Заказ не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus lastStatus = orderStatusService.getLastStatusById(orderId);

        if (lastStatus.getStatus() != 0) {
            attributes.addFlashAttribute("error", "Неверный статус заказа");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus orderStatus = new OrderStatus(1, order);
        orderStatusService.saveOrderStatus(orderStatus);

        return "redirect:/admin/order/details/" + order.getId();
    }

    @PostMapping("/admin/order/set-delivered-status")
    public String setDeliveredStatus(@RequestParam("orderId") int orderId, HttpServletRequest request, RedirectAttributes attributes) {

        Order order = orderService.getOrderId(orderId);

        if (order == null) {
            attributes.addFlashAttribute("error", "Заказ не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus lastStatus = orderStatusService.getLastStatusById(orderId);

        if (lastStatus.getStatus() != 1) {
            attributes.addFlashAttribute("error", "Неверный статус заказа");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus orderStatus = new OrderStatus(2, order);
        orderStatusService.saveOrderStatus(orderStatus);

        return "redirect:/admin/order/details/" + order.getId();
    }

    @PostMapping("/admin/order/set-completed-status")
    public String setComplitedStatus(@RequestParam("orderId") int orderId, HttpServletRequest request, RedirectAttributes attributes) {

        Order order = orderService.getOrderId(orderId);

        if (order == null) {
            attributes.addFlashAttribute("error", "Заказ не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus lastStatus = orderStatusService.getLastStatusById(orderId);

        if (lastStatus.getStatus() != 2) {
            attributes.addFlashAttribute("error", "Неверный статус заказа");
            return "redirect:" + request.getHeader("Referer");
        }

        OrderStatus orderStatus = new OrderStatus(3, order);
        orderStatusService.saveOrderStatus(orderStatus);

        return "redirect:/admin/order/details/" + order.getId();
    }
}
