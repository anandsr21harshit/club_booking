package com.booking.club.repository;

import com.booking.club.models.BookingDays;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface BookingDayRepository extends MongoRepository<BookingDays,String> {

     BookingDays findBookingDaysByYearAndMonth(String year,String month);

}
