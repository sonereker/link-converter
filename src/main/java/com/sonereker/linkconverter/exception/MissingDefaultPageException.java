package com.sonereker.linkconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no PageType implementation <code>isDefault()</code>
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MissingDefaultPageException extends RuntimeException {
    private static final String ERR_MSG = "Required page type 'Home' is missing.";

    public MissingDefaultPageException() {
        super(ERR_MSG);
    }
}
