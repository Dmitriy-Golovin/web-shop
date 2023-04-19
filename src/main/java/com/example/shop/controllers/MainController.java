package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("")
    public String getInfoApi() {
        return "Данное API предназначено для получения информации о товарах";
    }
}
