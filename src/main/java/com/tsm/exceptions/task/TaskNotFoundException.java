package com.tsm.exceptions.task;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class TaskNotFoundException extends BaseApiException {
    public TaskNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
