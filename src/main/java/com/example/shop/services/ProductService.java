package com.example.shop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.repositories.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
