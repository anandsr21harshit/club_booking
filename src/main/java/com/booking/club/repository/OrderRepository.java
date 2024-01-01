package com.booking.club.repository;

import com.booking.club.models.OrderEntity;
import com.razorpay.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity,String> {
}
