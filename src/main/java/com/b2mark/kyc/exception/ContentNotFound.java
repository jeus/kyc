package com.b2mark.kyc.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class ContentNotFound extends RuntimeException {

    public ContentNotFound() {
        super();
    }

    public ContentNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentNotFound(String message) {
        super(message);
    }

    public ContentNotFound(Throwable cause) {
        super(cause);
    }

}

