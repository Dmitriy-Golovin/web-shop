package com.example.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.models.Cart;
import com.example.shop.repositories.CartRepository;

@Service
@Transactional(readOnly = true)
public class CartService {
    

    private CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart findExist(Cart cart){
        Optional<Cart> cartExist = cartRepository.findByProductIdAndPersonId(cart.getProductId(), cart.getPersonId());
        return cartExist.orElse(null);
    }

    public List<Cart> getCartListByPersonId(int personIn) {
        return cartRepository.findByPersonId(personIn);
    }

    @Transactional
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }
}
