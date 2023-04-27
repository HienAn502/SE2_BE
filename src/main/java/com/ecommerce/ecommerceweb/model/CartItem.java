package com.ecommerce.ecommerceweb.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public CartItem(int id, int quantity, Product product, Cart cart) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;
    }

    public CartItem() {
    }

    public double getTotalPrice() {return quantity * product.getPrice();}
    public double getUnitPrice() {return product.getPrice();}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
