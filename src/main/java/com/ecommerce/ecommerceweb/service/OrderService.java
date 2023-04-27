package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.cart.CartDTO;
import com.ecommerce.ecommerceweb.datatransferobject.cart.CartItemDTO;
import com.ecommerce.ecommerceweb.model.Order;
import com.ecommerce.ecommerceweb.model.OrderDetail;
import com.ecommerce.ecommerceweb.model.User;
import com.ecommerce.ecommerceweb.repository.OrderDetailRepository;
import com.ecommerce.ecommerceweb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    public void saveOrder(CartDTO cartDTO, User user) {
        Order order = new Order();
        order.setOrderStatus("PENDING");
        order.setOrderDate(new Date());
        order.setUser(user);
        order.setTotalPrice(cartDTO.getTotalPrice());
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CartItemDTO cartItemDTO : cartDTO.getCartItemDTOList()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItemDTO.getProduct());
            orderDetail.setQuantity(cartItemDTO.getQuantity());
            orderDetail.setUnitPrice(cartItemDTO.getProduct().getPrice());
            orderDetail.setTotalPrice(cartItemDTO.totalPrice());

            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }

        cartDTO.setCartItemDTOList(new ArrayList<>());
        cartDTO.setTotalPrice(0);

        order.setOrderDetailList(orderDetailList);
        orderRepository.save(order);
    }
}
