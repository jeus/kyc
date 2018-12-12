/**
 * <h1></h1>
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.entity;

import com.b2mark.kyc.enums.Gender;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MerchantKyc {

    private String fname;
    private String lname;
    private Gender gender; //TODO: check how to get gender fromm system. kyc
    private String address;
    private String card; //TODO: check format card.
    private String nationalCode; //TODO: check size and format.
    private String cover; //TODO: check how it work in upload master.

}
