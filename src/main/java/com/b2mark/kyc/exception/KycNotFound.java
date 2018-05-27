package com.b2mark.kyc.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class KycNotFound extends RuntimeException {

    public KycNotFound() {
        super();
    }

    public KycNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public KycNotFound(String message) {
        super(message);
    }

    public KycNotFound(Throwable cause) {
        super(cause);
    }

}

