package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shop.security.PersonDetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class UserController {
    
    @GetMapping("/cart")
    public String cart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/user";
        }

        return "user/cart";
    }
}
