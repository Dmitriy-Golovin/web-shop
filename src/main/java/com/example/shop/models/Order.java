package com.example.shop.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.shop.enumm.OrderStatusList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    private String number;

    @Column(name = "count")
    private int count;

    @Column(name = "price")
    private float price;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Person person;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderStatus> orderStatusList = new ArrayList<>();

    @PrePersist
    private void init(){
        createdAt = LocalDateTime.now();
    }

    public Order(String number, int count, float price, Product product, Person person) {
        this.number = number;
        this.count = count;
        this.price = price;
        this.product = product;
        this.person = person;
    }

    public Order() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderStatus> getOrderStatusList() {
        return this.orderStatusList;
    }

    public void addOrderStatus(OrderStatus status) {
        status.setOrder(this);
        orderStatusList.add(status);
    }

    public String getStatusValue() {
        OrderStatusList[] statusList = OrderStatusList.values();
        return statusList[this.getOrderStatusList().size() - 1].toString();
    }
}
