package com.booking.club.service;

import com.booking.club.models.OrderEntity;
import com.booking.club.models.PaymentRequest;
import com.booking.club.models.User;
import com.booking.club.repository.OrderRepository;
import com.booking.club.repository.UserRepository;
import com.booking.club.utilities.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public String initiateOrder(PaymentRequest paymentRequest){
        try {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_9ClnnR46hUDqmA", "1OS2FwVw4iCdbKe3h9LdSQlo");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", paymentRequest.getAmount()*100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", Utility.generateReceiptId());
            Order order = razorpay.orders.create(orderRequest);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderId(order.get("id"));
            orderEntity.setAmount(order.get("amount"));
            orderEntity.setCreated_at(order.get("created_at"));
            orderEntity.setCurrency(order.get("currency"));
            orderEntity.setReceipt(order.get("receipt"));
            orderEntity.setUser(userRepository.findById(paymentRequest.getUserId()).get());
            orderEntity.setStatus(order.get("status"));
            orderEntity.setAttempts(order.get("attempts"));
            orderEntity.setPaymentId(null);

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

    public void updateOrderDetails(String paymentId,String orderId,String status){
        Query query = new Query();
        query.addCriteria(Criteria.where("orderId").is(orderId));

       Update update = new Update();
       update.set("paymentId",paymentId);
       update.set("status",status);

       mongoTemplate.updateFirst(query,update,OrderEntity.class);
    }

}
