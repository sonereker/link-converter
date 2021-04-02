package com.sonereker.linkconverter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.MalformedURLException;

/**
 * General REST exception handler
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MalformedURLException.class})
    public void handle(MalformedURLException e) {
        log.error("Exception: ", e);
    }
}
