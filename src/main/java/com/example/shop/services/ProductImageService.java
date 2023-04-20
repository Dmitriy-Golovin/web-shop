package com.example.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.models.ProductImage;
import com.example.shop.repositories.ProductImageRepository;

@Service
@Transactional(readOnly = true)
public class ProductImageService {
    
    private ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public List<ProductImage> getProductImageByProductIdAndType(int productId, int type) {
        return productImageRepository.findByProductIdAndType(productId, type);
    }

    public ProductImage getProductImageProductIdAndId(int productId, int id) {
        Optional<ProductImage> optionalProduct = productImageRepository.findByProductIdAndId(productId, id);

        return optionalProduct.orElse(null);
    }

    @Transactional
    public void deleteProductImage(int id) {
        productImageRepository.deleteById(id);
    }
}
