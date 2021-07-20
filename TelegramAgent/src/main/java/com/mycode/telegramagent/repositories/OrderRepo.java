package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
    void deleteAllByUserId(String uuid);
    Order getOrderByUserId(String uuid);
}
