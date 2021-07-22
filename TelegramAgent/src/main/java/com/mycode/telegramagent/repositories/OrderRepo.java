package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.models.UserRequest;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<UserRequest, Long> {
    void deleteAllByUserId(String uuid);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email " +
            "and u.user_id=:uuid", nativeQuery = true)
    UserRequest getOrderByUserId(String uuid,String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='De_Active'", nativeQuery = true)
    List<UserRequest> getAllActiveRequestByAgent(String email);
    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.request_status='De_Active'", nativeQuery = true)
    List<UserRequest> getAllArchivedRequests(String email);

    UserRequest getUserRequestByIdAndAgent_Email(Long id,String email);
}
