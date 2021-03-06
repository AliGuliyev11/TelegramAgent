package com.mycode.telegramagent.dao.Impl;

import com.google.gson.Gson;
import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.exceptions.RequestNotFound;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.mycode.telegramagent.utils.ExpiredDateGenerator.getExpiredDate;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Component
public class OrderDaoImpl implements OrderDAO {

    private final OrderRepo orderRepo;
    private final AgentRepo agentRepo;

    public OrderDaoImpl(OrderRepo orderRepo, AgentRepo agentRepo) {
        this.orderRepo = orderRepo;
        this.agentRepo = agentRepo;
    }

    @Value("${work.begin.time}")
    String beginTime;
    @Value("${work.end.time}")
    String endTime;
    @Value("${expired.time}")
    int expiredTime;
    @Value("${working.days}")
    String[] workingDays;

    /**
     * This method for adding request to every verified user
     *
     * @param order Map which getting from queue(answer of Bot's question )
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addOrder(Map<String, String> order) {
        Gson gson = new Gson();
        String json = gson.toJson(order);
        JSONObject jsonObject = new JSONObject(json);
        String uuid = jsonObject.getString("uuid");
        List<Agent> agentList = agentRepo.getVerifiedAgents();
        LocalDateTime expiredDate = getExpiredDate(beginTime, endTime, expiredTime, workingDays);
        for (var item : agentList) {
            UserRequest userRequest = new UserRequest();
            userRequest.setUserRequest(json);
            userRequest.setExpiredDate(expiredDate);
            userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
            userRequest.setRequestStatus(RequestStatus.Active);
            userRequest.setUserId(uuid);
            userRequest.setAgent(item);
            orderRepo.save(userRequest);

        }
    }

    /**
     * This method for save user request
     *
     * @param userRequest user request for save
     */

    public void saveUserRequest(UserRequest userRequest) {
        orderRepo.save(userRequest);
    }

    /**
     * This method for add user request to archive
     *
     * @param email current logged in agent's email
     * @param id    user request's id
     * @return UserRequest
     */

    @Override
    public UserRequest addToArchive(String email, Long id) {
        UserRequest userRequest = orderRepo.getUserRequestByIdAndAgent_Email(id, email);
        userRequest.setRequestStatus(RequestStatus.De_Active);
        orderRepo.save(userRequest);
        return userRequest;
    }

    /**
     * This method for getting user request of logged in agent
     *
     * @param id    user request id
     * @param email current logged in agent
     * @return UserRequest
     */

    @Override
    public UserRequest getUserRequestByIdAndAgentEmail(Long id, String email) {
        return orderRepo.getUserRequestByIdAndAgent_Email(id, email);
    }

    /**
     * Check expired  request and if user request expires set De_Active and Expired status
     *
     * @param date date time now
     */

    @Transactional
    @Override
    public void requestChecker(String date) {
        orderRepo.checkAndExpireRequest(date);
    }

    /**
     * This method for getting expired requests
     *
     * @param date date time now
     * @return List<UserRequest>
     * Need transactional for getting user request because user request has @Lob column
     * @apiNote With the help of this method if this method not return null it enter OrderLifeCycle class
     */

    @Transactional
    @Override
    public List<UserRequest> getExpiredRequests(String date) {
        return orderRepo.getExpiredUserRequest(date);
    }

    /**
     * This method for get all archived user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @apiNote Need transactional for getting user request because user request has @Lob column
     */

    @Override
    @Transactional
    public List<UserRequest> getAllArchive(String email) {
        return orderRepo.getAllArchivedRequests(email);
    }

    /**
     * This method for get getting user request by id
     *
     * @param id user request id
     * @return UserRequest
     */

    @Override
    public UserRequest getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(RequestNotFound::new);
    }

    /** This method for making request status De_Active
     * @param uuid user id */

    @Override
    public void requestStatusDeActive(String uuid) {
        orderRepo.getUserRequestByUserId(uuid);
    }

    /**
     * This method for get all user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @apiNote Need transactional for getting user request because user request has @Lob column
     */

    @Override
    @Transactional
    public List<UserRequest> getAllRequests(String email) {
        return orderRepo.getAllActiveRequestByAgent(email);
    }

    /**
     * This method for get all new type user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @apiNote Need transactional for getting user request because user request has @Lob column
     */

    @Override
    @Transactional
    public List<UserRequest> getAllNewRequests(String email) {
        return orderRepo.getAllNewRequestByAgent(email);
    }

    /**
     * This method for get all offer made user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @apiNote Need transactional for getting user request because user request has @Lob column
     */

    @Override
    @Transactional
    public List<UserRequest> getAllOfferMadeRequests(String email) {
        return orderRepo.getAllOfferMadeRequestByAgent(email);
    }

    /**
     * This method for get all accepted type user requests
     *
     * @param email current logged in agent's email
     * @return List<UserRequest>
     * @apiNote Need transactional for getting user request because user request has @Lob column
     */

    @Override
    @Transactional
    public List<UserRequest> getAllAcceptedRequests(String email) {
        return orderRepo.getAllAcceptedRequestByAgent(email);
    }

    /**
     * This method for delete request when expired date passed X days(see application.properties)
     *
     * @param day days between expired date and now
     */

    @Transactional
    @Override
    public void checkAndDeleteRequest(String day) {
        orderRepo.checkAndDeleteExpiredRequest(day);
    }

    /**
     * This method checks user request
     * @apiNote checks some of the agents made offer to this request or nor
     *
     * @param userId user id
     */

    @Override
    public Boolean checkAgentMadeOfferOrNot(String userId) {
        return orderRepo.checkOfferMadeOrNot(userId);
    }
}
