package com.example.shop.enumm;

public enum UserRoleSelect {

    ROLE_USER("Пользователь"),
    ROLE_ADMIN("Администратор");

    private String displayValue;

    UserRoleSelect(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
