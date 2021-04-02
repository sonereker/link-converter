package com.sonereker.linkconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when url/deepLink pattern is different from expected
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UrlPatternNotFoundException extends RuntimeException {
    private static final String ERR_MSG = "Provided url is not recognized as a valid url.";

    public UrlPatternNotFoundException() {
        super(ERR_MSG);
    }
}
