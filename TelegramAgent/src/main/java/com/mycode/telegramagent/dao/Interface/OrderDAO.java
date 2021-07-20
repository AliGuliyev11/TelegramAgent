package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.models.Order;

import java.util.List;

public interface OrderDAO {
    void addOrder(Order order);
    Order getOrderById(Long id);
    void deleteAllByUserId(String uuid);
    List<Order> getAllOrders();
}
