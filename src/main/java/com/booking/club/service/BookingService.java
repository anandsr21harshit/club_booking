package com.booking.club.service;

import com.booking.club.models.Booking;
import com.booking.club.models.BookingDays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class BookingService {

    @Autowired
    MongoTemplate mongoTemplate;


    public List<Integer> fetchAvailableDates(String month,String year){
        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year));
        query.addCriteria(Criteria.where("month").is(month));

        List<BookingDays> fetchedList = mongoTemplate.find(query,BookingDays.class);
        Map<Integer,String> dateMap = fetchedList.get(0).getAvailableDates();

        List<Integer> availableDates = new ArrayList<>();

        dateMap.forEach((key,value) -> {
            if(value.equals("available")) availableDates.add(key);
        });

        return availableDates;
    }

    public void lockDates(Booking booking) {
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();

        String startMonth = startDate.getMonth().toString();
        String endMonth = endDate.getMonth().toString();
        int startDay = startDate.getDayOfMonth();
        int endDay = endDate.getDayOfMonth();

        int year = startDate.getYear();

        if (startMonth.equals(endMonth)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("year").is(String.valueOf(year)));
            query.addCriteria(Criteria.where("month").is(startMonth));

            List<BookingDays> fetchedList = mongoTemplate.find(query, BookingDays.class);
            Map<Integer, String> dateMap = fetchedList.get(0).getAvailableDates();

            dateMap.forEach((key, value) -> {
                if (key >= startDay && key <= endDay) {
                    dateMap.put(key, "pending");
                }
            });

            Update update = new Update();
            update.set("availableDates", dateMap);
            mongoTemplate.updateFirst(query, update, BookingDays.class);

            log.info("Dates locked");
        }
    }

    public void releaseDate(Booking booking){
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();

        String startMonth = startDate.getMonth().toString();
        String endMonth = endDate.getMonth().toString();
        int startDay = startDate.getDayOfMonth();
        int endDay = endDate.getDayOfMonth();

        int year = startDate.getYear();

        if (startMonth.equals(endMonth)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("year").is(String.valueOf(year)));
            query.addCriteria(Criteria.where("month").is(startMonth));

            List<BookingDays> fetchedList = mongoTemplate.find(query, BookingDays.class);
            Map<Integer, String> dateMap = fetchedList.get(0).getAvailableDates();

            dateMap.forEach((key, value) -> {
                if (key >= startDay && key <= endDay) {
                    dateMap.put(key, "available");
                }
            });

            Update update = new Update();
            update.set("availableDates", dateMap);
            mongoTemplate.updateFirst(query, update, BookingDays.class);

            log.info("Dates locked");
        }
    }

    public void bookDates(Booking booking){
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();

        String startMonth = startDate.getMonth().toString();
        String endMonth = endDate.getMonth().toString();
        int startDay = startDate.getDayOfMonth();
        int endDay = endDate.getDayOfMonth();

        int year = startDate.getYear();

        if(startMonth.equals(endMonth)){
            Query query = new Query();
            query.addCriteria(Criteria.where("year").is(String.valueOf(year)));
            query.addCriteria(Criteria.where("month").is(startMonth));

            List<BookingDays> fetchedList = mongoTemplate.find(query,BookingDays.class);
            Map<Integer,String> dateMap = fetchedList.get(0).getAvailableDates();

            dateMap.forEach((key,value) -> {
                if(key >= startDay && key <= endDay ){
                    dateMap.put(key,"booked");
                }
            });

            Update update = new Update();
            update.set("availableDates",dateMap);
            mongoTemplate.updateFirst(query,update,BookingDays.class);

            log.info("Dates booked");
        }
    }



}
