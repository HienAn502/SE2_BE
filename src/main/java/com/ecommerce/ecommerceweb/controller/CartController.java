package com.ecommerce.ecommerceweb.controller;

import com.ecommerce.ecommerceweb.aGeneral.ApiResponse;
import com.ecommerce.ecommerceweb.datatransferobject.cart.AddItemToCartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartItemDTO;
import com.ecommerce.ecommerceweb.exception.ItemNotExistException;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.model.User;
import com.ecommerce.ecommerceweb.service.AuthenticationTokenService;
import com.ecommerce.ecommerceweb.service.CartService;
import com.ecommerce.ecommerceweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    // post method api for cart (add to cart)
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemToCartDTO addItemToCartDTO, @RequestParam("token") String token){
        // check if token is valid
        authenticationTokenService.authenticateToken(token);

        // check if user is valid then store user
        User user = authenticationTokenService.getUser(token);

        cartService.addToCart(addItemToCartDTO, user);

        return new ResponseEntity<>(new ApiResponse(true, "Item added to cart!"), HttpStatus.OK);
    }

    // get method api for cart (get all cart item)
    @GetMapping("/")
    public ResponseEntity<CartDTO> getAllCartItems(@RequestParam("token") String token){
        // check if token is valid
        authenticationTokenService.authenticateToken(token);

        // check if user is valid
        User user = authenticationTokenService.getUser(token);

        // get all items in cart
        CartDTO cartDTO = cartService.listAllCartItems(user);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{cartItemID}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable("cartItemID") int itemID, @RequestParam("token") String token, @RequestBody AddItemToCartDTO cartItemDTO) {
        // check if token is valid
        authenticationTokenService.authenticateToken(token);

        // check if user is valid
        User user = authenticationTokenService.getUser(token);

        try {
            cartService.updateItem(cartItemDTO, user);
        } catch (ItemNotExistException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid item id"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Item deleted from cart!"), HttpStatus.OK);
    }

    // delete cart method api
    @DeleteMapping("/delete/{cartItemID}")
    public ResponseEntity<ApiResponse> deleteCartItem (@PathVariable("cartItemID") Integer itemID, @RequestParam("token") String token){
        // check if token is valid
        authenticationTokenService.authenticateToken(token);

        // check if user is valid
        User user = authenticationTokenService.getUser(token);

        try {
            cartService.deleteItemFromCart(itemID, user);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid item id"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Item deleted from cart!"), HttpStatus.OK);
    }
}
