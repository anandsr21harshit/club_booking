package com.booking.club.controller;

import com.booking.club.models.User;
import com.booking.club.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User userDetails){
        log.info("<<< Inside registerUser controller >>>");
        try{
            User newUser = userService.addNewUser(userDetails);
            return new ResponseEntity<>(newUser,HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred while adding user details - ", ex);
        }
        return null;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> userDetails(@PathVariable String userId){
        log.info("<<< Inside userDetails controller >>>");
        try{
            User userDetails = userService.fetchUser(userId);
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred while fetching user details - ", ex);
        }
        return null;
    }
}
