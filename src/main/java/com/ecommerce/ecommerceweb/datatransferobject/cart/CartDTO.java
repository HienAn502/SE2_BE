package com.ecommerce.ecommerceweb.datatransferobject.cart;

import java.util.List;

public class CartDTO {
    private List<CartItemDTO> cartItemDTOList;
    private double totalPrice;

    public CartDTO() {
    }

    public List<CartItemDTO> getCartItemDTOList() {
        return cartItemDTOList;
    }

    public void setCartItemDTOList(List<CartItemDTO> cartItemDTOList) {
        this.cartItemDTOList = cartItemDTOList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
