package com.booking.club.controller;

import com.booking.club.models.LoginRequest;
import com.booking.club.models.LoginResponse;
import com.booking.club.models.User;
import com.booking.club.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    LoginResponse loginResponse;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequestBody){
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(loginRequestBody.getEmail()));
        List<User> fetchedUserList = mongoTemplate.find(query,User.class);
        if(fetchedUserList.isEmpty()){
            log.info("User is not registered");
            loginResponse.setMessage("User is not registered");
            loginResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(loginResponse, HttpStatus.NOT_FOUND);
        }else {
            User loggingUser = fetchedUserList.get(0);
            if(!Objects.equals(loginRequestBody.getPassword(), loggingUser.getPassword())){
                log.info("Incorrect password");
                loginResponse.setMessage("Incorrect password or email");
                loginResponse.setStatus(HttpStatus.UNAUTHORIZED);
                return new ResponseEntity<>(loginResponse,HttpStatus.UNAUTHORIZED);
            }else {
                JSONObject userInfo = new JSONObject();
                userInfo.put("name",loggingUser.getName());
                userInfo.put("userId",loggingUser.getUserId());
                log.info("User Logged in: " + userInfo.toString());
                loginResponse.setMessage(userInfo.toString());
                loginResponse.setStatus(HttpStatus.OK);
                return new ResponseEntity<>(loginResponse,HttpStatus.OK);
            }
        }


    }
}
