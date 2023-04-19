package com.example.shop.enumm;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum UserRole {
    Пользователь("ROLE_USER"),
    Администратор("ROLE_ADMIN");

    private final String roleName;

    private UserRole(String roleName) {
        this.roleName = roleName;
    }
    
    private final static Map<String, UserRole> roles = Arrays.stream(UserRole.values())
        .collect(Collectors.toMap(k->k.roleName, v->v));
    
    public static UserRole getRoleByName(String roleName) {
        return roles.get(roleName);        
    }
}
