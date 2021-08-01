package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.models.UserRequest;

import java.util.List;
import java.util.Map;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public interface OrderDAO {
    void addOrder(Map<String, String> order);

    UserRequest getOrderById(Long id);

    List<UserRequest> getAllRequests(String email);

    List<UserRequest> getAllNewRequests(String email);

    List<UserRequest> getAllOfferMadeRequests(String email);

    List<UserRequest> getAllAcceptedRequests(String email);

    UserRequest addToArchive(String email, Long id);

    List<UserRequest> getAllArchive(String email);

    void requestChecker(String date);

    List<UserRequest> getExpiredRequests(String date);

    void requestStatusDeActive(String uuid);

    UserRequest getUserRequestByIdAndAgentEmail(Long id, String email);

    void saveUserRequest(UserRequest userRequest);

    void checkAndDeleteRequest(String day);
    Boolean checkAgentMadeOfferOrNot(String day);
}
