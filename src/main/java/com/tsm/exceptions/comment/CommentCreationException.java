package com.tsm.exceptions.comment;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class CommentCreationException extends BaseApiException {
    public CommentCreationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}