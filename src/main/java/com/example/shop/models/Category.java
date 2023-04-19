package com.example.shop.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Category {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 5, max = 100, message = "Название категории должно быть от 5 до 100 символов")
    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Product> product;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void init(){
        createdAt = LocalDateTime.now();
    }

    public Category(String title, List<Product> product) {
        this.title = title;
        this.product = product;
    }

    public Category() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Product> getProduct() {
        return this.product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
