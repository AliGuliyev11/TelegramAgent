package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<UserRequest, Long> {

    @Modifying
    @Query(value = "update user_request u SET request_status='De_Active',agent_request_status='Expired' WHERE u.user_id=:uuid",nativeQuery = true)
    void getUserRequestByUserId(String uuid);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email " +
            "and u.user_id=:uuid", nativeQuery = true)
    UserRequest getOrderByUserId(String uuid, String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='De_Active'", nativeQuery = true)
    List<UserRequest> getAllActiveRequestByAgent(String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.request_status='De_Active'", nativeQuery = true)
    List<UserRequest> getAllArchivedRequests(String email);

    UserRequest getUserRequestByIdAndAgent_Email(Long id, String email);

    @Modifying
    @Query(value = "update user_request u SET request_status='De_Active',agent_request_status='Expired' " +
            "where CAST(TO_TIMESTAMP(CAST(u.expired_date as VARCHAR), 'YYYY-MM-DD hh24:MI') as TIMESTAMP)=CAST(:strDate as TIMESTAMP)", nativeQuery = true)
    void checkAndExpireRequest(String strDate);

}
