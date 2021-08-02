package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Repository of order repo
 */

public interface OrderRepo extends JpaRepository<UserRequest, Long> {

    @Modifying
    @Query(value = "update user_request u SET request_status='De_Active',agent_request_status='Expired' WHERE u.user_id=:uuid", nativeQuery = true)
    void getUserRequestByUserId(String uuid);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email " +
            "and u.user_id=:uuid", nativeQuery = true)
    UserRequest getOrderByUserId(String uuid, String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='De_Active'", nativeQuery = true)
    List<UserRequest> getAllActiveRequestByAgent(String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='De_Active' AND u.agent_request_status='New_Request'", nativeQuery = true)
    List<UserRequest> getAllNewRequestByAgent(String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='De_Active' AND u.agent_request_status='Offer_Made'", nativeQuery = true)
    List<UserRequest> getAllOfferMadeRequestByAgent(String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.agent_request_status!='Expired'and u.request_status!='De_Active' AND u.agent_request_status='Accepted'", nativeQuery = true)
    List<UserRequest> getAllAcceptedRequestByAgent(String email);

    @Query(value = "SELECT * FROM user_request u JOIN agent a ON u.agent_id=a.id where a.email=:email and " +
            "u.request_status='De_Active'", nativeQuery = true)
    List<UserRequest> getAllArchivedRequests(String email);

    UserRequest getUserRequestByIdAndAgent_Email(Long id, String email);

    @Modifying
    @Query(value = "update user_request u SET request_status='De_Active',agent_request_status='Expired' " +
            "where CAST(TO_TIMESTAMP(CAST(u.expired_date as VARCHAR), 'YYYY-MM-DD hh24:MI') as TIMESTAMP)=CAST(:strDate as TIMESTAMP)", nativeQuery = true)
    void checkAndExpireRequest(String strDate);

    @Query(value = "SELECT DISTINCT ON (user_id) *  from user_request u where " +
            "CAST(TO_TIMESTAMP(CAST(u.expired_date as VARCHAR)," +
            " 'YYYY-MM-DD hh24:MI') as TIMESTAMP)=CAST(:date as TIMESTAMP)", nativeQuery = true)
    List<UserRequest> getExpiredUserRequest(String date);

    @Modifying
    @Query(value = "DELETE FROM user_request u where (CAST(u.expired_date as DATE)=cast(:date as date)" +
            " OR CAST(u.expired_date as DATE)<cast(:date as date)) AND u.agent_request_status='Expired' " +
            "and u.request_status='De_Active'", nativeQuery = true)
    void checkAndDeleteExpiredRequest(String date);

    @Query(value = "SELECT DISTINCT u.expired_date FROM user_request u JOIN agent a ON u.agent_id=a.id where " +
            "u.user_id=:userId and (u.agent_request_status='Offer_Made' or u.agent_request_status='Accepted')", nativeQuery = true)
    LocalDateTime getUserRequestLocalDatetimeByUserID(String userId);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * from user_request u where " +
            "CAST(TO_TIMESTAMP(CAST(u.expired_date as VARCHAR),'YYYY-MM-DD hh24:MI') as TIMESTAMP)=CAST(:date as TIMESTAMP)" +
            " AND (u.agent_request_status='Offer_Made' or u.agent_request_status='Accepted' ))THEN true ELSE FALSE END", nativeQuery = true)
    Boolean checkOfferMadeOrNot(String date);

}
