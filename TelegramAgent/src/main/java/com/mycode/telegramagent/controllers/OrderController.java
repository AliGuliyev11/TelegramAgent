package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.models.Order;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final IOrderService service;

    public OrderController(IOrderService service) {
        this.service = service;
    }

    @GetMapping("/showAll")
    public List<Order> getAllOrders(){
        return service.getAllOrders();
    }





}
