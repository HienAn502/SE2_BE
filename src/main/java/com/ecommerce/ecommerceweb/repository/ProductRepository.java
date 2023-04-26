package com.ecommerce.ecommerceweb.repository;

import com.ecommerce.ecommerceweb.model.Category;
import com.ecommerce.ecommerceweb.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where p.category = ?1")
    Page<Product> searchProductByCategory(Category category, Pageable pageable);
    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    Page<Product> searchProducts(String keyword, Pageable pageable);
}
