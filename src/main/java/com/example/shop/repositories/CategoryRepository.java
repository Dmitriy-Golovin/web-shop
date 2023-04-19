package com.example.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shop.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
}
