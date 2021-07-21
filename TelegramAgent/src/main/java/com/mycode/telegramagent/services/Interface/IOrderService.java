package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.models.UserRequest;

import java.util.List;

public interface IOrderService {
    void addOrder(Order order);
    UserRequest getOrderById(Long id);
//    void deleteAllByUserId(String uuid);
    List<UserRequest> getAllRequests(String email);
}
