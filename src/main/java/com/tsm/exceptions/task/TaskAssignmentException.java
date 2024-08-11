package com.tsm.exceptions.task;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class TaskAssignmentException extends BaseApiException {
    public TaskAssignmentException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
