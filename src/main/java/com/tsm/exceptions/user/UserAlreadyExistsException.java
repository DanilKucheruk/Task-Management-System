package com.tsm.exceptions.user;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class UserAlreadyExistsException extends BaseApiException{
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}