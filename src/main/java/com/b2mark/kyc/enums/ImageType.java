/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.enums;

import java.text.MessageFormat;

public enum ImageType {
    cover("cover"),
    passport("passport"),
    passid("passid");


    private String imageType;


    ImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getStatus() {
        return this.imageType;
    }

    public String getDirectory() {return MessageFormat.format("/{0}", this.imageType);}

    public static ImageType fromString(String imageType) {
        for (ImageType b : ImageType.values()) {
            if (b.imageType.equalsIgnoreCase(imageType)) {
                return b;
            }
        }
        return null;
    }

}
