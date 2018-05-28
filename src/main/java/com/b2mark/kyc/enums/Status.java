/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.enums;

public enum Status {
    not_active("not_active"),
    pending("pending"),
    accepted("accepted"),
    rejected("rejected");


    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static Status fromString(String status) {
        for (Status b : Status.values()) {
            if (b.status.equalsIgnoreCase(status)) {
                return b;
            }
        }
        return null;
    }
}
