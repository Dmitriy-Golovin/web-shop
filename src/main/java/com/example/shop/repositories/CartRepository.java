package com.example.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shop.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    Optional<Cart> findByProductIdAndPersonId(int productId, int personId);

    List<Cart> findByPersonId(int personId);
}
