package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Value;
import com.example.shop.models.Category;
import com.example.shop.models.Product;
import com.example.shop.models.ProductImage;
import com.example.shop.services.CategoryService;
import com.example.shop.services.ProductImageService;
import com.example.shop.services.ProductService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
public class AdminProductController {
    
    private ProductService productService;

    private CategoryService categoryService;

    private ProductImageService productImageService;

    @Value("${upload.path}")
    private String uploadPath;

    public AdminProductController(ProductService productService, CategoryService categoryService, ProductImageService productImageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productImageService = productImageService;
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
    public String newProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file, @RequestParam("category") String category) throws IOException {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Добавить товар");
            model.addAttribute("categoryList", categoryService.getAllCategory());
            return "admin/product/add";
        }

        if (category.isEmpty()) {
            model.addAttribute("title", "Добавить товар");
            model.addAttribute("categoryList", categoryService.getAllCategory());
            model.addAttribute("error_category", "Категория не может быть пустой");
            return "admin/product/add";
        }

        int categoryInt = Integer.parseInt(category);
        Category categoryExist = categoryService.getCategoryId(categoryInt);

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
        return "redirect:/admin/product/details/" + product.getId();
    }

    @GetMapping("/admin/product/details/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Product productDetails = productService.getProductId(id);

        if (productDetails == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Товар не найден"
            );
        }

        model.addAttribute("product", productDetails);
        model.addAttribute("title", "Товар: " + productDetails.getTitle());
        return "admin/product/details";
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductId(id);

        if (product == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Товар не найден"
            );
        }

        model.addAttribute("product", product);
        model.addAttribute("title", "Редактировать товар: " + product.getTitle());
        model.addAttribute("categoryList", categoryService.getAllCategory());
        return "admin/product/edit";
    }

    @PostMapping("/admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file, @RequestParam("category") String category, @PathVariable("id") int id) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Редактировать товар: " + product.getTitle());
            model.addAttribute("categoryList", categoryService.getAllCategory());
            return "admin/product/edit";
        }

        if (category.isEmpty()) {
            model.addAttribute("title", "Редактировать товар: " + product.getTitle());
            model.addAttribute("categoryList", categoryService.getAllCategory());
            model.addAttribute("error_category", "Категория не может быть пустой");
            return "admin/product/add";
        }

        int categoryInt = Integer.parseInt(category);
        Category categoryExist = categoryService.getCategoryId(categoryInt);

        if (categoryExist == null) {
            model.addAttribute("title", "Редактировать товар: " + product.getTitle());
            model.addAttribute("categoryList", categoryService.getAllCategory());
            model.addAttribute("error_category", "Категория не найдена");
            return "admin/product/add";
        }

        if (file.getSize() > 0) {
            ProductImage existProductImage = productImageService.getProductImageByProductIdAndType(id, ProductImage.getMainType()).get(0);
            String existFileName = existProductImage.getFileName();
            File existFile = new File(uploadPath + "/" + existFileName);
            productImageService.deleteProductImage(existProductImage.getId());
            existFile.delete();

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
        }

        productService.editProduct(product, id, categoryExist);
        return "redirect:/admin/product/details/" + product.getId();
    }

    @PostMapping("/admin/product/add-secondary-img/{id}")
    public String addSecondaryImage(@PathVariable("id") int id, Model model, @RequestParam("file") MultipartFile file, HttpServletRequest request, RedirectAttributes attributes) throws IOException {
        Product product = productService.getProductId(id);

        if (product == null) {
            attributes.addFlashAttribute("error_file", "Товар не найден");
            return "redirect:" + request.getHeader("Referer");
        }

        if (file.getSize() > 0) {
            List<ProductImage> imageList = product.getProductImageList();

            if (imageList.size() >= 5) {
                attributes.addFlashAttribute("error_file", "Добавлено максимальное количество изображений для товара - 5");
                return "redirect:" + request.getHeader("Referer");
            }

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
            image.setSecondaryType();
            product.addImageToProduct(image);

            productService.editProduct(product, id, product.getCategory());
        }

        return "redirect:/admin/product/details/" + product.getId();
    }

    @GetMapping("/admin/product/delete-secondary-img/{productId}/{imgId}")
    public String deleteSecondaryImage(@PathVariable("productId") int productId, @PathVariable("imgId") int imgId, HttpServletRequest request, RedirectAttributes attributes) {

        Product product = productService.getProductId(productId);

        if (product == null) {
            attributes.addFlashAttribute("error_file", "Товар не найден, удаление изображения невозможно");
            return "redirect:" + request.getHeader("Referer");
        }

        ProductImage productImage = productImageService.getProductImageProductIdAndId(productId, imgId);

        if (productImage == null) {
            attributes.addFlashAttribute("error_file", "ТИзображение товара не найдено");
            return "redirect:" + request.getHeader("Referer");
        }

        String fileName = productImage.getFileName();
        File file = new File(uploadPath + "/" + fileName);
        productImageService.deleteProductImage(productImage.getId());
        file.delete();

        return "redirect:/admin/product/details/" + product.getId();
    }
    
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, HttpServletRequest request, RedirectAttributes attributes) {

        Product product = productService.getProductId(id);

        if (product == null) {
            attributes.addFlashAttribute("error_file", "Товар не найден, удаление невозможно");
            return "redirect:" + request.getHeader("Referer");
        }

        productService.deleteProduct(id);

        return "redirect:/admin/product";
    }
}
