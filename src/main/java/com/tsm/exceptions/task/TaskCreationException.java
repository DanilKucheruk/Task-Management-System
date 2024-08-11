package com.tsm.exceptions.task;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class TaskCreationException extends BaseApiException {
    public TaskCreationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
