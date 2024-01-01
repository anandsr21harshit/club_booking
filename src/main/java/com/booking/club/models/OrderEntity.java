package com.booking.club.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "Order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEntity {

    @MongoId
    @Field("_id")
    String id;
    int amount;
    int amount_paid;
    Object notes;
    int created_at;
    int amount_due;
    String currency;
    String receipt;
    String entity;
    String offer_id;
    String status;
    int attempts;

}
