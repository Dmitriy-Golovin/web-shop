package com.example.shop.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.shop.models.Cart;
import com.example.shop.models.Product;
import com.example.shop.security.PersonDetails;
import com.example.shop.services.CategoryService;
import com.example.shop.services.ProductService;
import com.example.shop.specifications.ProductSpecification;

@Controller
public class MainController {

    ProductService productService;

    CategoryService categoryService;

    public MainController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
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
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("title", "Товары");
        return "front/product/list";
    }

    @PostMapping("/")
    public String search(Model model, ProductSpecification filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() == "anonymousUser") {
            model.addAttribute("auth", false);
        } else {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            model.addAttribute("auth", true);
            model.addAttribute("user", personDetails.getPerson());
        }

        model.addAttribute("productList", productService.getProductByFilter(filter));
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("title", "Товары");
        return "front/product/list";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable("id") int id, Model model) throws ResponseStatusException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Product product = productService.getProductId(id);

        if (product == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Товар не найден"
            );
        }

        if (authentication.getPrincipal() == "anonymousUser") {
            model.addAttribute("auth", false);
        } else {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            Cart cart = new Cart();
            cart.setProductId(product.getId());
            model.addAttribute("auth", true);
            model.addAttribute("user", personDetails.getPerson());
            model.addAttribute("cart", cart);
        }

        model.addAttribute("product", product);
        model.addAttribute("title", product.getTitle());
        return "front/product/details";
    }
}
