package com.example.shop.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

@Entity
public class ProductImage {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "type", nullable = false, columnDefinition = "smallint")
    private int type;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void init(){
        createdAt = LocalDateTime.now();
    }

    @Transient
    private static int mainType = 1;

    @Transient
    private static int secondaryType = 2;

    public ProductImage(String fileName, Integer type, Product product) {
        this.fileName = fileName;
        this.type = type;
        this.product = product;
    }

    public ProductImage() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMainType() {
        this.type = 1;
    }

    public void setSecondaryType() {
        this.type = 2;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static int getMainType() {
        return mainType;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", type='" + getType() + "'" +
            ", product='" + getProduct() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
