package com.ecommerce.ecommerceweb.datatransferobject.cart;

import com.ecommerce.ecommerceweb.model.Cart;
import com.ecommerce.ecommerceweb.model.CartItem;
import com.ecommerce.ecommerceweb.model.Product;

public class CartItemDTO {
    private Integer id;
    private Integer quantity;
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CartItemDTO() {

    }

    public double totalPrice() {
        if (this.product.getVoucher() != null && !this.product.getVoucher().isExpired())
            return this.quantity * this.product.getDiscountPrice();
        return this.quantity * this.product.getPrice();
    }

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.product = cartItem.getProduct();
        this.quantity = cartItem.getQuantity();
    }
}
