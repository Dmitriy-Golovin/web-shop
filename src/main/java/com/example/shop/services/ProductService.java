package com.example.shop.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.models.Category;
import com.example.shop.models.Product;
import com.example.shop.repositories.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductId(int id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Transactional
    public void saveProduct(Product product, Category category) {
        product.setCategory(category);
        productRepository.save(product);
    }
}
