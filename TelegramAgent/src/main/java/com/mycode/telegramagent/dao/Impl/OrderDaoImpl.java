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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderDaoImpl implements OrderDAO {

    private final OrderRepo orderRepo;
    private final AgentRepo agentRepo;
    private ModelMapper modelMapper = new ModelMapper();

    public OrderDaoImpl(OrderRepo orderRepo, AgentRepo agentRepo) {
        this.orderRepo = orderRepo;
        this.agentRepo = agentRepo;
    }

    @Override
    public void addOrder(Order order) {
        UserRequest userRequest = modelMapper.map(order, UserRequest.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        List<Agent> agentList=agentRepo.getVerifiedAgents();
        for (var item:agentList){
            userRequest.setExpiredDate(LocalDateTime.now().plusHours(8));
            userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
            userRequest.setRequestStatus(RequestStatus.Active);
            userRequest.setAgent(item);
            orderRepo.save(userRequest);

        }
    }

    @Override
    public UserRequest getOrderById(Long id) {
        return orderRepo.findById(id).get();
    }

    @Override
    public void deleteAllByUserId(String uuid) {
        if (orderRepo.getOrderByUserId(uuid) != null) {
            orderRepo.deleteAllByUserId(uuid);
        }
    }

    @Override
    public List<UserRequest> getAllOrders() {
        return orderRepo.findAll();
    }
}
