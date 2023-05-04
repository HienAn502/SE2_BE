package com.ecommerce.ecommerceweb.repository;

import com.ecommerce.ecommerceweb.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
