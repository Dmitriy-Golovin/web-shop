package com.example.shop.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.shop.models.Product;
import com.example.shop.security.PersonDetails;
import com.example.shop.services.ProductService;

@Controller
public class MainController {

    ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() == "anonymousUser") {
            model.addAttribute("auth", false);
        } else {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            model.addAttribute("auth", true);
            model.addAttribute("user", personDetails.getPerson());
        }
        
        model.addAttribute("productList", productService.getAllProduct());
        model.addAttribute("title", "Товары");
        return "front/product/list";
    }
}
