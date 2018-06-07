/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.enums;

import java.nio.file.Path;
import java.nio.file.Paths;


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

    public Path getPath(Path rootLocation) {
        StringBuilder sb = new StringBuilder(
                rootLocation.toAbsolutePath().toString())
                .append("/").append(this.imageType);

        return Paths.get(sb.toString());
    }


    public static ImageType fromString(String imageType) {
        for (ImageType b : ImageType.values()) {
            if (b.imageType.equalsIgnoreCase(imageType)) {
                return b;
            }
        }
        return null;
    }

}
