package com.booking.club.controller;

import com.booking.club.models.Booking;
import com.booking.club.repository.BookingDayRepository;
import com.booking.club.repository.BookingRepository;
import com.booking.club.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class BookingController {

    @Autowired
    BookingDayRepository bookingDayRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingService bookingService;

    @GetMapping("/available_dates")
    public List<Integer> getAvailableDates(@RequestParam String month, @RequestParam String year) {
       List<Integer> availableDates = bookingService.fetchAvailableDates(month,year);
       log.info(availableDates.toString());
       return availableDates;
    }

    @PostMapping("/lock_dates")
    public void lockDateSlots(@RequestBody Booking booking){
        //book dates
        bookingService.lockDates(booking);
    }

    @PostMapping("/release_lock")
    public void releaseDateSlots(@RequestBody Booking booking){
        bookingService.releaseDate(booking);
    }

    // Book slots
    // 1. Update available date in BookingDaysAvailability collection
    // 2. Add data in Booking collection
    @PostMapping("/book")
    public void bookSlots(@RequestBody Booking booking){
        //book dates
        bookingService.bookDates(booking);
        // add new record in Booking collection
        bookingRepository.save(booking);
    }

}
