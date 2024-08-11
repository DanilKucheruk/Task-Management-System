package com.tsm.exceptions.comment;

import org.springframework.http.HttpStatus;

import com.tsm.exceptions.BaseApiException;

public class CommentUpdateException extends BaseApiException {
    public CommentUpdateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}