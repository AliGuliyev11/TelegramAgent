package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public void addOrder(Map<String, String> order) {
        orderDAO.addOrder(order);
    }

    @Override
    public UserRequest getOrderById(Long id) {
        return orderDAO.getOrderById(id);
    }

    @Override
    @Transactional
    public UserRequest addToArchive(String email, Long id) {
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(id, email);
        if (userRequest == null) {
            throw new RequestNotFound();
        }

        return orderDAO.addToArchive(email, id);
    }

    @Override
    @Transactional
    public UserRequest moveFromArchive(String email, Long id) {
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(id, email);
        if (userRequest == null) {
            throw new RequestNotFound();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Expired)) {
            throw new RequestExpired();
        }
        userRequest.setRequestStatus(RequestStatus.Active);
        orderDAO.saveUserRequest(userRequest);
        return userRequest;
    }


    @Override
    public List<UserRequest> getAllArchive(String email) {
        List<UserRequest> userRequests = orderDAO.getAllArchive(email);
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

    @Override
    public List<UserRequest> getAllNewRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllNewRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    @Override
    public List<UserRequest> getAllOfferMadeRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllOfferMadeRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    @Override
    public List<UserRequest> getAllAcceptedRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllAcceptedRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }
}
