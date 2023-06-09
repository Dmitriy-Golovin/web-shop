package com.example.shop.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.example.shop.models.Category;
import com.example.shop.models.Product;
import com.example.shop.services.CategoryService;

import jakarta.validation.Valid;

@Controller
public class AdminCategoryController {
    
    private CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/category")
    public String index(Model model) {
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("title", "Список категорий");

        return "admin/category/list";
    }

    @GetMapping("/admin/category/add")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("title", "Добавить категорию");
        return "admin/category/add";
    }

    @PostMapping("/admin/category/add")
    public String newCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Добавить категорию");
            return "admin/category/add";
        }

        categoryService.categoryAdd(category);
        return "redirect:/admin/category/details/" + category.getId();
    }

    @GetMapping("/admin/category/details/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Category categoryDetails = categoryService.getCategoryId(id);

        if (categoryDetails == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Категория не найденf"
            );
        }

        model.addAttribute("category", categoryDetails);
        model.addAttribute("title", "Категория: " + categoryDetails.getTitle());
        return "admin/category/details";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String editCategory(@PathVariable("id") int id, Model model){
        Category categoryEdit = categoryService.getCategoryId(id);

        if (categoryEdit == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, "Категория не найденf"
            );
        }

        model.addAttribute("category", categoryEdit);
        model.addAttribute("title", "Редактировать категорию: " + categoryEdit.getTitle());
        return "admin/category/edit";
    }

    @PostMapping("/admin/category/edit/{id}")
    public String editCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Редактировать категорию: " + categoryService.getCategoryId(id).getTitle());
            return "admin/category/edit";
        }

        categoryService.categoryEdit(id, category);
        return "redirect:/admin/category/details/" + categoryService.getCategoryId(id).getId();
    }

    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id, Model model){
        Category category = categoryService.getCategoryId(id);
        List<Product> productList = category.getProduct();

        if (productList.size() > 0) {
            model.addAttribute("category", category);
            model.addAttribute("title", "Категория: " + category.getTitle());
            model.addAttribute("error", "Невозможно удалить категорию с товарами");

            return "admin/category/details";
        }

        categoryService.delete(id);
        return "redirect:/admin/category";
    }
}
