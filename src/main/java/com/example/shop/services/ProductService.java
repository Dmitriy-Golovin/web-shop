package com.example.shop.services;

import java.io.File;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.models.Category;
import com.example.shop.models.Product;
import com.example.shop.models.ProductImage;
import com.example.shop.repositories.ProductRepository;
import com.example.shop.specifications.ProductSpecification;

import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    @Value("${upload.path}")
    private String uploadPath;

    public ProductService(ProductRepository productRepository, ProductImageService productImageService) {
        this.productRepository = productRepository;
        this.productImageService = productImageService;
    }

    public Product getProductId(int id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> getProductByFilter(ProductSpecification filter) {
        return productRepository.findAll(filter);
    }

    @Transactional
    public void saveProduct(Product product, Category category) {
        product.setCategory(category);
        productRepository.save(product);
    }

    @Transactional
    public void editProduct(Product product, int id, Category category) {
        product.setId(id);
        product.setCategory(category);
        product.setCreatedAt(this.getProductId(id).getCreatedAt());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(int id) {
        List<ProductImage> imageList = this.getProductId(id).getProductImageList();

        for (ProductImage image: imageList) {
            String fileName = image.getFileName();
            File file = new File(uploadPath + "/" + fileName);
            file.delete();
        }

        productRepository.deleteById(id);
    }
}
