package com.ecommerce.ecommerceweb.repository;

import com.ecommerce.ecommerceweb.model.Cart;
import com.ecommerce.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
    Cart findByUser(User user);
}
