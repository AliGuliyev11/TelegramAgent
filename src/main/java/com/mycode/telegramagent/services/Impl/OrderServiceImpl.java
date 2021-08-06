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

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * This method for adding request to every verified user
     *
     * @param order Map which getting from queue(answer of Bot's question )
     */

    @Override
    public void addOrder(Map<String, String> order) {
        orderDAO.addOrder(order);
    }

    /**
     * This method for get getting user request by id
     *
     * @param id user request id
     * @return UserRequest
     */

    @Override
    public UserRequest getOrderById(Long id) {
        return orderDAO.getOrderById(id);
    }

    /**
     * This method for add user request to archive
     *
     * @param email current logged in agent's email
     * @param id    user request's id
     *              Need transactional for getting user request because user request has @Lob column
     * @return UserRequest
     * @throws RequestNotFound if not found any request
     */

    @Override
    @Transactional
    public UserRequest addToArchive(String email, Long id) {
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(id, email);
        if (userRequest == null) {
            throw new RequestNotFound();
        }

        return orderDAO.addToArchive(email, id);
    }


    /**
     * This method fro add user request to incoming requests from archived
     *
     * @param email current logged in agent's email
     * @param id    user request's id
     *              Need transactional for getting user request because user request has @Lob column
     * @return UserRequest
     * @throws RequestNotFound if not found any request
     * @throws RequestExpired  if agent request status is expired
     * @throws RequestAccepted if agent request status is accpeted
     */


    @Override
    @Transactional
    public UserRequest moveFromArchive(String email, Long id) {
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(id, email);
        if (userRequest == null) {
            throw new RequestNotFound();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Expired)) {
            throw new RequestExpired();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Accepted)) {
            throw new RequestAccepted();
        }
        userRequest.setRequestStatus(RequestStatus.Active);
        orderDAO.saveUserRequest(userRequest);
        return userRequest;
    }


    /**
     * This method for get all archived user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @throws NotAnyRequest if not found any request
     */

    @Override
    public List<UserRequest> getAllArchive(String email) {
        List<UserRequest> userRequests = orderDAO.getAllArchive(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    /**
     * This method for making request status De_Active
     *
     * @param uuid user id
     */

    @Override
    public void requestStatusDeActive(String uuid) {
        orderDAO.requestStatusDeActive(uuid);
    }

    /**
     * This method for get all user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @throws NotAnyRequest if not found any request
     */

    @Override
    public List<UserRequest> getAllRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    /**
     * This method for get all new type user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @throws NotAnyRequest if not found any request
     */

    @Override
    public List<UserRequest> getAllNewRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllNewRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    /**
     * This method for get all offer made user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @throws NotAnyRequest if not found any request
     */

    @Override
    public List<UserRequest> getAllOfferMadeRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllOfferMadeRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }

    /**
     * This method for get all accepted type user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @throws NotAnyRequest if not found any request
     */

    @Override
    public List<UserRequest> getAllAcceptedRequests(String email) {
        List<UserRequest> userRequests = orderDAO.getAllAcceptedRequests(email);
        if (userRequests.isEmpty()) {
            throw new NotAnyRequest();
        }
        return userRequests;
    }
}
