package com.booking.club.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@Document(collection = "User")
public class User {

    @MongoId
    @Field("_id")
    String userId;
    String name;
    String email;
    String password;
    long phoneNumber;
}
