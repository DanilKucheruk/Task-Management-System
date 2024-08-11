package com.tsm.exceptions.user;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class UserCreationException extends BaseApiException{
    public UserCreationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}