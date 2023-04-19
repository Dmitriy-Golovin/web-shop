package com.example.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shop.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
}
