package com.booking.club.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;

@Document(collection = "BookingDaysAvailability")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class BookingDays {

    private String year;
    private String month;
    private Map<Integer,String> availableDates;
}
