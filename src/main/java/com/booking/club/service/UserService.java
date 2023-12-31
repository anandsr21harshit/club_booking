package com.booking.club.service;

import com.booking.club.models.User;
import com.booking.club.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User addNewUser(User userDetails){
        User savedUser = null;
        try {
            savedUser = userRepository.save(userDetails);
            log.info("Saved User: " + savedUser);
        }catch (Exception ex){
            log.error("Error occurred while saving data - " + ex.getMessage(),ex);
        }

        return savedUser;
    }

    public User fetchUser(String userId){
        // logic
        Optional<User> userDetails = null;
        try {
            userDetails = userRepository.findById(userId);
            log.info("Fetched user: " + userDetails.get());
        }catch (Exception ex){
            log.error("Error occurred while fetching details - " + ex.getMessage(),ex);
        }
        return userDetails.get();
    }
}
