package com.example.shop.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.shop.models.Cart;
import com.example.shop.services.CartService;

@Component
public class CartValidator implements Validator {

    private CartService cartService;

    public CartValidator(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Cart.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Cart cart = (Cart) target;

        if(cartService.findExist(cart) != null){
            errors.rejectValue("productId", "", "Товар уже находится в корзине");
        }
    }
    
}
