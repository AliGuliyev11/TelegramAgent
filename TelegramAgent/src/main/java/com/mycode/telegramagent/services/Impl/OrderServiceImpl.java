package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.models.UserRequest;
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
    public UserRequest getOrderById(Long id) {
        return orderDAO.getOrderById(id);
    }

    @Override
    public UserRequest addToArchive(String email, Long id) {
        return orderDAO.addToArchive(email, id);
    }

    @Override
    public List<UserRequest> getAllArchive(String email) {
        return orderDAO.getAllArchive(email);
    }

    //    @Override
//    public void deleteAllByUserId(String uuid) {
//        orderDAO.deleteAllByUserId(uuid);
//    }

    @Override
    public List<UserRequest> getAllRequests(String email) {
        return orderDAO.getAllRequests(email);
    }
}
