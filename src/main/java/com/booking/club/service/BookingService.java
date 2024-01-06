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
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class BookingService {

    @Autowired
    MongoTemplate mongoTemplate;


    public List<Integer> fetchAvailableDates(String month,String year){

        month = month.toUpperCase();
        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year));
        query.addCriteria(Criteria.where("month").is(month));

        List<BookingDays> fetchedList = mongoTemplate.find(query,BookingDays.class);
        Map<Integer,String> dateMap = fetchedList.get(0).getAvailableDates();

        List<Integer> availableDates = new ArrayList<>();

        dateMap.forEach((key,value) -> {
            if(value.equals("booked") || value.equals("pending")) availableDates.add(key);
        });

        return availableDates;
    }

    public void lockDates(Booking booking) {
        String startDateString = booking.getStartDate();
        String endDateString = booking.getEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString,formatter);
        LocalDate endDate = LocalDate.parse(endDateString,formatter);

        int startYear = startDate.getYear();
        String startMonth = startDate.getMonth().toString();
        int startDay = startDate.getDayOfMonth();

        int endYear = endDate.getYear();
        String endMonth = endDate.getMonth().toString();
        int endDay = endDate.getDayOfMonth();


        if ((startMonth.equals(endMonth)) && (startYear == endYear)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("year").is(String.valueOf(startYear)));
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
        String startDateString = booking.getStartDate();
        String endDateString = booking.getEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString,formatter);
        LocalDate endDate = LocalDate.parse(endDateString,formatter);

        int startYear = startDate.getYear();
        String startMonth = startDate.getMonth().toString();
        int startDay = startDate.getDayOfMonth();

        int endYear = endDate.getYear();
        String endMonth = endDate.getMonth().toString();
        int endDay = endDate.getDayOfMonth();

        if (startMonth.equals(endMonth) && startYear == endYear) {
            Query query = new Query();
            query.addCriteria(Criteria.where("year").is(String.valueOf(startYear)));
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

            log.info("Dates released");
        }
    }


    // max booking 1 month
    public void bookDates(Booking booking){
        String startDateString = booking.getStartDate();
        String endDateString = booking.getEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString,formatter);
        LocalDate endDate = LocalDate.parse(endDateString,formatter);

        int startYear = startDate.getYear();
        String startMonth = startDate.getMonth().toString();
        int startDay = startDate.getDayOfMonth();

        int endYear = endDate.getYear();
        String endMonth = endDate.getMonth().toString();
        int endDay = endDate.getDayOfMonth();

        if(startMonth.equals(endMonth) && startYear == endYear){
            Query query = new Query();
            query.addCriteria(Criteria.where("year").is(String.valueOf(startYear)));
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
