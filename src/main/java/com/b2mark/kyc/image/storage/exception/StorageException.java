/**
 * @author becopay
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.image.storage.exception;

public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
