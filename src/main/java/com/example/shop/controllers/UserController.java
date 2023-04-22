package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shop.models.Cart;
import com.example.shop.models.Order;
import com.example.shop.models.OrderStatus;
import com.example.shop.models.Product;
import com.example.shop.security.PersonDetails;
import com.example.shop.services.CartService;
import com.example.shop.services.OrderService;
import com.example.shop.services.ProductService;
import com.example.shop.util.CartValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class UserController {

    private CartValidator cartValidator;

    private CartService cartService;

    private ProductService productService;

    private OrderService orderService;

    public UserController(CartValidator cartValidator, CartService cartService, ProductService productService, OrderService orderService) {
        this.cartValidator = cartValidator;
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
    }
    
    @GetMapping("/auth")
    public String auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/user";
        }

        if (role.equals("ROLE_USER")) {
            return "redirect:/cart";
        }

        return "front/user/cart";
    }

    @PostMapping("/cart/add")
    public String addCart(@ModelAttribute("cart") @Valid Cart cart, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes attributes) {

        Product product = productService.getProductId(cart.getProductId());

        if (product == null) {
            attributes.addFlashAttribute("error", "Товар не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        cart.setPersonId(personDetails.getPerson().getId());
        cartValidator.validate(cart, bindingResult);
        
        if(bindingResult.hasErrors()){
            model.addAttribute("auth", true);
            model.addAttribute("user", personDetails.getPerson());
            model.addAttribute("product", product);
            model.addAttribute("title", product.getTitle());
            return "front/product/details";
        }

        cartService.saveCart(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Cart> cartList = cartService.getCartListByPersonId(personDetails.getPerson().getId());
        List<Product> productList = new ArrayList<>();

        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }

        model.addAttribute("productList", productList);
        model.addAttribute("title", "Корзина");

        return "front/user/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String cartDeleteItem(@PathVariable("id") int productId, Model model, RedirectAttributes attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Cart cartItem = cartService.findExist(new Cart(personDetails.getPerson().getId(), productId));

        if (cartItem == null) {
            attributes.addFlashAttribute("error_delete", "Товар в корзине не найден");
            return "redirect:/cart";
        }

        cartService.deleteCart(cartItem.getId());
        return "redirect:/cart";
    }

    @PostMapping("/order/add")
    public String addOrder(@RequestParam("productId") int productId, @RequestParam("count") int count, HttpServletRequest request, RedirectAttributes attributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Product product = productService.getProductId(productId);
        

        if (product == null) {
            attributes.addFlashAttribute("error", "Товар не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        Cart cartItem = cartService.findExist(new Cart(personDetails.getPerson().getId(), product.getId() ));

        if (cartItem == null) {
            attributes.addFlashAttribute("error", "Товар в козине не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        float price = 0;

        for (int i = 0; i < count; i++) {
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();
        Order order = new Order(uuid, count, price, product, personDetails.getPerson());
        OrderStatus status = new OrderStatus();
        status.setStatus(0);
        order.addOrderStatus(status);

        cartService.deleteCart(cartItem.getId());
        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String order(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderService.getByPersonId(personDetails.getPerson().getId());
        float mainSum = 0;

        for (Order order: orderList) {
            mainSum += order.getPrice();
        }

        model.addAttribute("orderList", orderList);
        model.addAttribute("title", "Ваши заказы");
        model.addAttribute("mainSum", mainSum);
        return "front/user/order";
    }
}
