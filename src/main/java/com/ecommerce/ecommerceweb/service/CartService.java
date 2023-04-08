package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.cart.AddItemToCartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartItemDTO;
import com.ecommerce.ecommerceweb.exception.CommonException;
import com.ecommerce.ecommerceweb.model.Cart;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.model.User;
import com.ecommerce.ecommerceweb.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    ProductService productService;

    @Autowired
    CartRepository cartRepository;

    public void addToCart(AddItemToCartDTO addItemToCartDTO, User user) {
        // check if product chosen is valid
        Product product = productService.findById(addItemToCartDTO.getProductId());

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(addItemToCartDTO.getQuantity());
        cart.setCreatedDate(new Date());

        // save cart state
        cartRepository.save(cart);
    }

    public CartDTO listAllCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDTO> cartItem = new ArrayList<>();
        double totalPrice = 0;

        for (Cart cart : cartList){
            CartItemDTO cartItemDTO = new CartItemDTO(cart);
            totalPrice += cart.getProduct().getPrice() * cartItemDTO.getQuantity();

            cartItem.add(cartItemDTO);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartItemDTOList(cartItem);
        cartDTO.setTotalPrice(totalPrice);
        return cartDTO;
    }

    public void deleteItemFromCart(Integer itemID, User user) {
        // check if user has the item
        Optional<Cart> optionalCart = cartRepository.findById(itemID);
        if (optionalCart.isEmpty()){
            throw new CommonException("Cart item is invalid! Item id:" + itemID);
        }

        Cart cart = optionalCart.get();
        if (cart.getUser() != user) {
            throw new CommonException("This item doesn't belong to this user! Item id: " + itemID);
        }

        cartRepository.delete(cart);
    }
}
