package com.ecommerce.ecommerceweb.repository;

import com.ecommerce.ecommerceweb.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
