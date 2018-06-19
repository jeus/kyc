/**
 * <h1>Exception when data not found</h1>
 * <p> This excption when call that information not found in database.
 * <b>HTTP CODE:</b>204 no_content
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class Unauthorized extends RuntimeException {

    public Unauthorized() {
        super();
    }

    public Unauthorized(String message, Throwable cause) {
        super(message, cause);
    }

    public Unauthorized(String message) {
        super(message);
    }

    public Unauthorized(Throwable cause) {
        super(cause);
    }

}

