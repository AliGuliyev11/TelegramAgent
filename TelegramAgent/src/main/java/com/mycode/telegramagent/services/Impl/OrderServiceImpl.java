package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.models.Order;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public void addOrder(Order order) {
        orderDAO.addOrder(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderDAO.getOrderById(id);
    }

    @Override
    public void deleteAllByUserId(String uuid) {
        orderDAO.deleteAllByUserId(uuid);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}
