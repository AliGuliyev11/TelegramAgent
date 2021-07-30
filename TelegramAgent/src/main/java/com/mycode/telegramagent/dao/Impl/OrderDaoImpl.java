package com.mycode.telegramagent.dao.Impl;

import com.google.gson.Gson;
import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
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
 * */

@Component
public class OrderDaoImpl implements OrderDAO {

    private final OrderRepo orderRepo;
    private final AgentRepo agentRepo;
    private ModelMapper modelMapper = new ModelMapper();

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addOrder(Map<String, String> order) {
        Gson gson = new Gson();
        String json = gson.toJson(order);
        JSONObject jsonObject=new JSONObject(json);
        String uuid=jsonObject.getString("uuid");
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

    public void saveUserRequest(UserRequest userRequest) {
        orderRepo.save(userRequest);
    }

    @Override
    public UserRequest addToArchive(String email, Long id) {
        UserRequest userRequest = orderRepo.getUserRequestByIdAndAgent_Email(id, email);
        userRequest.setRequestStatus(RequestStatus.De_Active);
        orderRepo.save(userRequest);
        return userRequest;
    }

    @Override
    public UserRequest getUserRequestByIdAndAgentEmail(Long id, String email) {
        return orderRepo.getUserRequestByIdAndAgent_Email(id, email);
    }

    @Transactional
    @Override
    public void requestChecker(String date) {
        orderRepo.checkAndExpireRequest(date);
    }

    @Transactional
    @Override
    public List<UserRequest> getExpiredRequests(String date) {
        return orderRepo.getExpiredUserRequest(date);
    }

    @Override
    @Transactional
    public List<UserRequest> getAllArchive(String email) {
        return orderRepo.getAllArchivedRequests(email);
    }

    @Override
    public UserRequest getOrderById(Long id) {
        return orderRepo.findById(id).get();
    }

    @Override
    public void requestStatusDeActive(String uuid) {
        orderRepo.getUserRequestByUserId(uuid);
    }

    @Override
    @Transactional
    public List<UserRequest> getAllRequests(String email) {
        return orderRepo.getAllActiveRequestByAgent(email);
    }

    @Override
    @Transactional
    public List<UserRequest> getAllNewRequests(String email) {
        return orderRepo.getAllNewRequestByAgent(email);
    }

    @Override
    @Transactional
    public List<UserRequest> getAllOfferMadeRequests(String email) {
        return orderRepo.getAllOfferMadeRequestByAgent(email);
    }

    @Override
    @Transactional
    public List<UserRequest> getAllAcceptedRequests(String email) {
        return orderRepo.getAllAcceptedRequestByAgent(email);
    }
}
