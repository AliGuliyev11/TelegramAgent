package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.exceptions.NotAnyRequest;
import com.mycode.telegramagent.exceptions.RequestNotFound;
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
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(id, email);
        if (userRequest==null){
            throw new RequestNotFound();
        }

        return orderDAO.addToArchive(email, id);
    }


    @Override
    public List<UserRequest> getAllArchive(String email) {
        List<UserRequest> userRequests=orderDAO.getAllArchive(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    @Override
    public void requestStatusDeActive(String uuid) {
        orderDAO.requestStatusDeActive(uuid);
    }

    @Override
    public List<UserRequest> getAllRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }
}
