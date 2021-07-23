package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.mycode.telegramagent.utils.ExpiredDateGenerator.getExpiredDate;

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

    @Override
    public void addOrder(Order order) {
        UserRequest userRequest = modelMapper.map(order, UserRequest.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        List<Agent> agentList = agentRepo.getVerifiedAgents();
        LocalDateTime expiredDate=getExpiredDate(beginTime,endTime,expiredTime);
        for (var item : agentList) {
            userRequest.setExpiredDate(expiredDate);
            userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
            userRequest.setRequestStatus(RequestStatus.Active);
            userRequest.setAgent(item);
            orderRepo.save(userRequest);

        }
    }

    @Override
    public UserRequest addToArchive(String email, Long id) {
        UserRequest userRequest =orderRepo.getUserRequestByIdAndAgent_Email(id, email);
        userRequest.setRequestStatus(RequestStatus.De_Active);
        orderRepo.save(userRequest);
        return userRequest;
    }

    @Transactional
    @Override
    public void requestChecker(String date) {
        orderRepo.checkAndExpireRequest(date);
    }

    @Override
    public List<UserRequest> getAllArchive(String email) {
        return orderRepo.getAllArchivedRequests(email);
    }

    @Override
    public UserRequest getOrderById(Long id) {
        return orderRepo.findById(id).get();
    }

//    @Override
//    public void deleteAllByUserId(String uuid) {
//        if (orderRepo.getOrderByUserId(uuid) != null) {
//            orderRepo.deleteAllByUserId(uuid);
//        }
//    }

    @Override
    public List<UserRequest> getAllRequests(String email) {
        return orderRepo.getAllActiveRequestByAgent(email);
    }
}
