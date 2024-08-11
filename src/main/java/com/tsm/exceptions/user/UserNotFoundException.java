package com.tsm.exceptions.user;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}