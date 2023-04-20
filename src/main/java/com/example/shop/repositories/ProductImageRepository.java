package com.example.shop.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.shop.models.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    

    List<ProductImage> findByProductIdAndType(int productId, int type);

    Optional<ProductImage> findByProductIdAndId(int productId, int id);
}
