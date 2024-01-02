package com.booking.club.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Document(collection = "Booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @MongoId
    @Field("_id")
    private String id;

    private String userId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String amenities;
}
