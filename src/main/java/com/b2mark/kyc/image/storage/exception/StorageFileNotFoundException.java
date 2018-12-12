/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.image.storage.exception;


public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}