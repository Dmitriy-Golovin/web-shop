package com.example.shop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.repositories.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
