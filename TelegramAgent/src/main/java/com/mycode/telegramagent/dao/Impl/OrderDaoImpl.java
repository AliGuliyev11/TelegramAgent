package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.models.Order;
import com.mycode.telegramagent.repositories.OrderRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDaoImpl implements OrderDAO {

    private final OrderRepo orderRepo;

    public OrderDaoImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public void addOrder(Order order) {
        orderRepo.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepo.findById(id).get();
    }

    @Override
    public void deleteAllByUserId(String uuid) {
        if (orderRepo.getOrderByUserId(uuid) != null) {
            orderRepo.deleteAllByUserId(uuid);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
