package com.ecommerce.ecommerceweb.repository;

import com.ecommerce.ecommerceweb.model.Cart;
import com.ecommerce.ecommerceweb.model.CartItem;
import com.ecommerce.ecommerceweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartAndAndProduct(Cart cart, Product product);
}
