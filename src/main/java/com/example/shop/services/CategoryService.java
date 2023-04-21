package com.example.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.models.Category;
import com.example.shop.repositories.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategoryId(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElse(null);
    }

    @Transactional
    public void categoryAdd(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void categoryEdit(int id, Category category) {
        category.setId(id);
        Category categoryExist = this.getCategoryId(id);
        category.setCreatedAt(categoryExist.getCreatedAt());
        categoryRepository.save(category);
    }
    
    @Transactional
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
