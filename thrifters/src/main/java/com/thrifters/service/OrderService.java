package com.thrifters.service;

import com.thrifters.exception.OrderException;
import com.thrifters.model.Address;
import com.thrifters.model.Order;
import com.thrifters.model.User;
import com.thrifters.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    public Order findOrderById(Long orderId )throws OrderException {

    }
}
