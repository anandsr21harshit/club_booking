package com.booking.club.models;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Data
public class LoginResponse {
    HttpStatus status;
    Object message;
}
