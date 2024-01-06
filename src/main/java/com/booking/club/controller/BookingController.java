package com.booking.club.controller;

import com.booking.club.models.Booking;
import com.booking.club.repository.BookingDayRepository;
import com.booking.club.repository.BookingRepository;
import com.booking.club.service.BookingService;
import com.booking.club.utilities.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // call this endpoint after 8min of login
    @PostMapping("/release_lock")
    public void releaseDateSlots(@RequestBody Booking booking){
        bookingService.releaseDate(booking);
    }

    // Book slots
    // 1. Update available date in BookingDaysAvailability collection
    // 2. Add data in Booking collection
    @PostMapping("/book")
    public void bookSlots(@RequestBody Booking booking){
        //book dates and update calendar
        bookingService.bookDates(booking);

        // add new record in Booking collection
        bookingRepository.save(booking);
    }

    @GetMapping("/all_bookings")
    public ResponseEntity<?> fetchAllBookings(){
        List<Booking> allBookings = bookingRepository.findAll();
        log.info("All bookings - " + allBookings);
        Utility.convertJsonToCSV(allBookings);
        if(allBookings.isEmpty()){
            return new ResponseEntity<>("No bookings present currently", HttpStatus.OK);
        }else {
            return new ResponseEntity<>(allBookings,HttpStatus.OK);
        }
    }

}
