package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.dto.Order;
import com.mycode.telegramagent.models.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<UserRequest, Long> {
    void deleteAllByUserId(String uuid);
    Order getOrderByUserId(String uuid);
}
