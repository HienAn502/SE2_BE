package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.cart.AddItemToCartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartItemDTO;
import com.ecommerce.ecommerceweb.exception.CommonException;
import com.ecommerce.ecommerceweb.exception.ItemNotExistException;
import com.ecommerce.ecommerceweb.model.Cart;
import com.ecommerce.ecommerceweb.model.CartItem;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.model.User;
import com.ecommerce.ecommerceweb.repository.CartItemRepository;
import com.ecommerce.ecommerceweb.repository.CartRepository;
import com.ecommerce.ecommerceweb.repository.ProductRepository;
import com.ecommerce.ecommerceweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    public void addToCart(AddItemToCartDTO addItemToCartDTO, User user) {
        // check if product chosen is valid
        Product product = productService.findById(addItemToCartDTO.getProductId());
        // get user's cart
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setCreatedDate(new Date());
        }

        List<CartItem> cartItems = cart.getCartItemList();
        CartItem cartItem = findCartItem(cartItems, product.getId());

        if (cartItems == null) {
            cartItems = new ArrayList<>();
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(addItemToCartDTO.getQuantity());
                cartItem.setCart(cart);

                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(addItemToCartDTO.getQuantity());
                cartItem.setCart(cart);

                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + addItemToCartDTO.getQuantity());
                cartItemRepository.save(cartItem);
            }
        }

        cart.setCartItemList(cartItems);
        cart.setUser(user);

        cartRepository.save(cart);
    }

    public CartDTO listAllCartItems(User user) {
        // find cart of user
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) return new CartDTO();

        List<CartItemDTO> cartItemsDTO = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cart.getCartItemList()){
            CartItemDTO cartItemDTO = new CartItemDTO(cartItem);
            totalPrice += cartItemDTO.totalPrice();

            cartItemsDTO.add(cartItemDTO);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartItemDTOList(cartItemsDTO);
        cartDTO.setTotalPrice(totalPrice);
        return cartDTO;
    }

    public void updateItem(AddItemToCartDTO cartItemDTO, User user) {
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItemList();
        CartItem item = findCartItem(cartItems, cartItemDTO.getProductId());
        if (item == null) throw new ItemNotExistException("Item not exist");

        item.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(item);
    }

    public void deleteItemFromCart(Integer itemID, User user) {
        // check if user has the item
        Optional<CartItem> optionalCart = cartItemRepository.findById(itemID);
        if (optionalCart.isEmpty()){
            throw new CommonException("Cart item is invalid! Item id:" + itemID);
        }

        CartItem cart = optionalCart.get();
        if (cart.getCart().getUser() != user) {
            throw new CommonException("This item doesn't belong to this user! Item id: " + itemID);
        }

        cartItemRepository.delete(cart);
    }

    public void saveCart(CartDTO cartDTO, User user) {
        Cart cart = user.getCart();
        if (cart == null) cart = new Cart();

        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemDTO cartItemDTO : cartDTO.getCartItemDTOList()) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(cartItemDTO.getProduct());
            cartItem.setQuantity(cartItemDTO.getQuantity());

            cartItems.add(cartItem);
        }
        cart.setCartItemList(cartItems);
        cart.setUser(user);

        cartRepository.save(cart);
    }

    private CartItem findCartItem(List<CartItem> cartItems, int productId) {
        if (cartItems == null) return null;
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId)
                cartItem = item;
        }
        return cartItem;
    }
}
