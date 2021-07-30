package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.models.UserRequest;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    void addOrder(Map<String, String> order);
    UserRequest getOrderById(Long id);
    List<UserRequest> getAllRequests(String email);
    List<UserRequest> getAllNewRequests(String email);
    List<UserRequest> getAllOfferMadeRequests(String email);
    List<UserRequest> getAllAcceptedRequests(String email);
    UserRequest moveFromArchive(String email, Long id);
    UserRequest addToArchive(String email,Long id);
    List<UserRequest> getAllArchive(String email);
    void requestStatusDeActive(String uuid);
}
