package com.ecommerce.ecommerceweb.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double rate;
    private Date createdDate;
    private Date expiredDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voucher")
    private List<Product> appliedProducts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public List<Product> getAppliedProducts() {
        return appliedProducts;
    }

    public void setAppliedProducts(List<Product> appliedProducts) {
        this.appliedProducts = appliedProducts;
    }

    public boolean isExpired() {
        return expiredDate.compareTo(new Date()) <= 0;
    }
}
