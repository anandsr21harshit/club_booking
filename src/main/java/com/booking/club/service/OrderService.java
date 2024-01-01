package com.booking.club.service;

import com.booking.club.models.OrderEntity;
import com.booking.club.models.PaymentRequest;
import com.booking.club.repository.OrderRepository;
import com.booking.club.utilities.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public String initiateOrder(PaymentRequest paymentRequest){
        try {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_9ClnnR46hUDqmA", "1OS2FwVw4iCdbKe3h9LdSQlo");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", paymentRequest.getAmount()*100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", Utility.generateReceiptId());
            Order order = razorpay.orders.create(orderRequest);

            ObjectMapper objectMapper = new ObjectMapper();
            OrderEntity orderEntity = objectMapper.readValue(order.toString(),OrderEntity.class);

            log.info(orderEntity.toString());
//          add logic to save data in Order table
            orderRepository.save(orderEntity);

            return order.toString();
        } catch (RazorpayException e) {
            // Handle Exception
            log.error("RazorpayException occurred while creating order - " + e.getMessage(),e);
            return e.getMessage();
        } catch (Exception e){
            log.error("Exception occurred while creating order -" + e.getMessage(),e);
            return e.getMessage();
        }
    }

}
