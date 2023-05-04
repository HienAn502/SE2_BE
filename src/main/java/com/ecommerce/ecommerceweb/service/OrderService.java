package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.checkout.CheckoutItemDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Value("${BASE_URL}")
    String baseURL;
    @Value("${STRIPE_SECRET_KEY}")
    String apiKey;

//    public void saveOrder(CartDTO cartDTO, User user) {
//        Order order = new Order();
//        order.setOrderStatus("PENDING");
//        order.setOrderDate(new Date());
//        order.setUser(user);
//        order.setTotalPrice(cartDTO.getTotalPrice());
//        List<OrderDetail> orderDetailList = new ArrayList<>();
//
//        for (CartItemDTO cartItemDTO : cartDTO.getCartItemDTOList()) {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrder(order);
//            orderDetail.setProduct(cartItemDTO.getProduct());
//            orderDetail.setQuantity(cartItemDTO.getQuantity());
//            orderDetail.setUnitPrice(cartItemDTO.getProduct().getPrice());
//            orderDetail.setTotalPrice(cartItemDTO.totalPrice());
//
//            orderDetailRepository.save(orderDetail);
//            orderDetailList.add(orderDetail);
//            cartService.deleteItemFromCart(cartItemDTO.getId(), user);
//        }
//
//        cartDTO.setCartItemDTOList(new ArrayList<>());
//        cartDTO.setTotalPrice(0);
//        order.setOrderDetailList(orderDetailList);
//
//        cartService.saveCart(cartDTO, user);
//        orderRepository.save(order);
//    }

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
