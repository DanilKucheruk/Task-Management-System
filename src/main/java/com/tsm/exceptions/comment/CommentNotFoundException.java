package com.tsm.exceptions.comment;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class CommentNotFoundException extends BaseApiException {
    public CommentNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}