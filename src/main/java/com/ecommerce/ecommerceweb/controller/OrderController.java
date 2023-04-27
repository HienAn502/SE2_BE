package com.ecommerce.ecommerceweb.controller;

import com.ecommerce.ecommerceweb.datatransferobject.checkout.CheckoutItemDTO;
import com.ecommerce.ecommerceweb.datatransferobject.checkout.StripeResponse;
import com.ecommerce.ecommerceweb.service.AuthenticationTokenService;
import com.ecommerce.ecommerceweb.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    AuthenticationTokenService authenticationTokenService;
    @Autowired
    OrderService orderService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {
        Session session = orderService.createSession(checkoutItemDTOList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }
}
