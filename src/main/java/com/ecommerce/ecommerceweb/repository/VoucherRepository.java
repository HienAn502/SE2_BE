package com.ecommerce.ecommerceweb.repository;

import com.ecommerce.ecommerceweb.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
