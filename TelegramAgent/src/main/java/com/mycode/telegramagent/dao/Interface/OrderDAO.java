package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.models.UserRequest;

import java.util.List;

public interface OrderDAO {
    void addOrder(Order order);
    UserRequest getOrderById(Long id);
    List<UserRequest> getAllRequests(String email);
    UserRequest addToArchive(String email,Long id);
    List<UserRequest> getAllArchive(String email);
    void requestChecker(String date);
    void requestStatusDeActive(String uuid);

}
