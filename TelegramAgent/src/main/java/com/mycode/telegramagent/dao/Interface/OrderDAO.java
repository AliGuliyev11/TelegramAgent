package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.models.UserRequest;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    void addOrder(Map<String, String> order);

    UserRequest getOrderById(Long id);

    List<UserRequest> getAllRequests(String email);

    UserRequest addToArchive(String email, Long id);

    List<UserRequest> getAllArchive(String email);

    void requestChecker(String date);
    List<UserRequest> getExpiredRequests(String date);

    void requestStatusDeActive(String uuid);

    UserRequest getUserRequestByIdAndAgentEmail(Long id, String email);

    void saveUserRequest(UserRequest userRequest);
}
