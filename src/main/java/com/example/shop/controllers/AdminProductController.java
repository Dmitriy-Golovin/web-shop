package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import com.example.shop.models.Category;
import com.example.shop.models.Product;
import com.example.shop.models.ProductImage;
import com.example.shop.services.CategoryService;
import com.example.shop.services.ProductService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import jakarta.validation.Valid;

@Controller
public class AdminProductController {
    
    private ProductService productService;

    private CategoryService categoryService;

    @Value("${upload.path}")
    private String uploadPath;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/product")
    public String index(Model model) {
        model.addAttribute("productList", productService.getAllProduct());
        model.addAttribute("title", "Список товаров");

        return "admin/product/list";
    }

    @GetMapping("/admin/product/add")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Добавить товар");
        model.addAttribute("categoryList", categoryService.getAllCategory());
        return "admin/product/add";
    }

    @PostMapping("/admin/product/add")
    public String newProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file, @RequestParam("category") int category) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Добавить товар");
            model.addAttribute("categoryList", categoryService.getAllCategory());
            return "admin/product/add";
        }

        Category categoryExist = categoryService.getCategoryId(category);

        if (categoryExist == null) {
            model.addAttribute("title", "Добавить товар");
            model.addAttribute("categoryList", categoryService.getAllCategory());
            model.addAttribute("error_category", "Категория не найдена");
            return "admin/product/add";
        }

        if (file.getSize() > 0) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setFileName(resultFileName);
            image.setMainType();
            product.addImageToProduct(image);
        } else {
            model.addAttribute("title", "Добавить товар");
            model.addAttribute("categoryList", categoryService.getAllCategory());
            model.addAttribute("error_file", "Основное изображение обязательно для товара");
            return "admin/product/add";
        }

        productService.saveProduct(product, categoryExist);

        return "admin/product/details/" + product.getId();
    }
}
