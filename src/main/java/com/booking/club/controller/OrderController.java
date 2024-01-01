package com.booking.club.controller;

import com.booking.club.models.PaymentRequest;
import com.booking.club.service.OrderService;
import com.booking.club.utilities.Utility;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody PaymentRequest paymentRequest){
        log.info("<<< Inside Create Order Controller >>>");
        return orderService.initiateOrder(paymentRequest);
    }

    @PostMapping("/update_order")
    public void updateOrder(@RequestBody Map<String,String> updateRequest){
        String orderId = updateRequest.get("orderId");
        String status = updateRequest.get("status");
        String paymentId = updateRequest.get("paymentId");
        orderService.updateOrderDetails(paymentId,orderId,status);
    }

}
