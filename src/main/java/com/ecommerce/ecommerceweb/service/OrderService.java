package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.cart.CartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartItemDTO;
import com.ecommerce.ecommerceweb.datatransferobject.checkout.CheckoutItemDTO;
import com.ecommerce.ecommerceweb.model.*;
import com.ecommerce.ecommerceweb.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Value("${BASE_URL}")
    String baseURL;
    @Value("${STRIPE_SECRET_KEY}")
    String apiKey;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    private double getTotalPrice(List<CheckoutItemDTO> checkoutItemDTOList) {
        double totalPrice = 0;
        for (CheckoutItemDTO checkoutItemDTO : checkoutItemDTOList.toArray(new CheckoutItemDTO[0])) {
            totalPrice += checkoutItemDTO.getPrice() * checkoutItemDTO.getQuantity();
        }
        return totalPrice;
    }
    public void saveOrder(List<CheckoutItemDTO> checkoutItemDTOList) {
        User user = userRepository.findById(checkoutItemDTOList.get(0).getUserId()+1).orElseThrow();
        Order order = new Order();
        order.setOrderStatus("PENDING");
        order.setOrderDate(new Date());
        order.setUser(user);
        order.setTotalPrice(getTotalPrice(checkoutItemDTOList));
        System.out.println(getTotalPrice(checkoutItemDTOList));
        List<OrderDetail> orderDetailList = new ArrayList<>();

        Cart cart = user.getCart();
        orderRepository.save(order);

        for (CheckoutItemDTO checkoutItemDTO : checkoutItemDTOList.toArray(new CheckoutItemDTO[0])) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = productRepository.findById(checkoutItemDTO.getProductId()).orElseThrow();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(checkoutItemDTO.getQuantity());
            orderDetail.setUnitPrice(checkoutItemDTO.getPrice());
            orderDetail.setTotalPrice(checkoutItemDTO.getPrice() * checkoutItemDTO.getQuantity());

            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }

        cart.setCartItemList(new ArrayList<>());
        order.setOrderDetailList(orderDetailList);

        cartService.deleteItemOfUser(user);
        cartService.saveNewCart(cart);
        orderRepository.save(order);
    }

    public Session createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {
       // success and failure urls
       String successURL = baseURL + "payment/success";
       String failureURL = baseURL + "payment/failed";

        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        for (CheckoutItemDTO checkoutItemDTO : checkoutItemDTOList) {
            sessionItemList.add(createSessionLineItem(checkoutItemDTO));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .setSuccessUrl(successURL)
                .addAllLineItem(sessionItemList)
                .build();

        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDTO checkoutItemDTO) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDTO))
                .setQuantity((long) checkoutItemDTO.getQuantity())
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDTO checkoutItemDTO) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) checkoutItemDTO.getPrice())
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(checkoutItemDTO.getProductName())
                        .build())
                .build();
    }

}
