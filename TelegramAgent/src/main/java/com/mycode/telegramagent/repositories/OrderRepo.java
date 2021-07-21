package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.models.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<UserRequest, Long> {
    void deleteAllByUserId(String uuid);

    UserRequest getOrderByUserId(String uuid);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='Expired'", nativeQuery = true)
    List<UserRequest> getAllActiveRequestByAgent(String email);
}
