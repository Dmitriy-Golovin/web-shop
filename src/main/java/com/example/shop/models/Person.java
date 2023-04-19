package com.example.shop.models;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shop.enumm.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

@Entity
@Table(name = "Person")
public class Person {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 5, max = 100, message = "Логин должен быть от 5 до 100 символов")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "ФИО не может быть пустым")
    @Size(min = 5, max = 100, message = "ФИО должно быть от 5 до 100 символов")
    @Column(name = "full_name")
    private String fullName;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "password")
    @Pattern(regexp = "^(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+)).*$", message = "Пароль должен содержать 1 заглавную букву 1 строчную и 1 цифру, всего не менее 6 символов")
    private String password;

    @Column(name = "role")
    // @NotEmpty(message = "Роль не может быть пустой")
    private String role;

    @ManyToMany()
    @JoinTable(name = "cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Order> orderList;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void init(){
        createdAt = LocalDateTime.now();
    }

    public Person(String login, String fullName, String password, String role) {
        this.login = login;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
    }

    public Person() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserRole getRoleValue() {
        String role = this.getRole();
        return UserRole.getRoleByName(role);
    }
}
