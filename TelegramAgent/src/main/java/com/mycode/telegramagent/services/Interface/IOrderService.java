package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.models.Order;

import java.util.List;

public interface IOrderService {
    void addOrder(Order order);
    Order getOrderById(Long id);
    void deleteAllByUserId(String uuid);
    List<Order> getAllOrders();
}
