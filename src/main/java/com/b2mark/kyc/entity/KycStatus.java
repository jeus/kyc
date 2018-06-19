package com.b2mark.kyc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

@Setter
@Getter
@AllArgsConstructor
public class KycStatus {
    @NotNull
    private String uid;
    @NotNull
    private com.b2mark.kyc.enums.Status status;

    public String toString()
    {
        return String.format("KYC Information:[ UId=%s,status:%s ]" +
                uid,status);
    }

}
