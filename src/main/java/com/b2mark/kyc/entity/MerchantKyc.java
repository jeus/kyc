/**
 * <h1></h1>
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.entity;

import com.b2mark.kyc.entity.tables.Kycinfo;
import com.b2mark.kyc.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MerchantKyc {

    @JsonIgnore
    private Kycinfo kycinfo;

    public MerchantKyc() {
        kycinfo = new Kycinfo();
    }

    public MerchantKyc(Kycinfo kycinfo) {
        this.kycinfo = kycinfo;
    }


    public void setKycinfo(Kycinfo kycinfo) {
        this.kycinfo = kycinfo;
    }

    public String getFname() {
        return kycinfo.getFname();
    }

    public void setFname(String fname) {
        kycinfo.setFname(fname);
    }

    public String getLname() {
        return kycinfo.getLname();
    }

    public void setLname(String lname) {
        kycinfo.setLname(lname);
    }

    public Gender getGender() {
        return kycinfo.getGender();
    }

    public void setGender(Gender gender) {
        kycinfo.setGender(gender);
    }

    public String getAddress() {
        return kycinfo.getAddress();
    }

    public void setAddress(String address) {
        kycinfo.setAddress(address);
    }

    public String getCard() {
        return kycinfo.getCard();
    } //TODO: check format card.

    public void setCard(String card) {
        kycinfo.setCard(card);
    }


    public String getNationalCode() {
        return kycinfo.getLicenseid();
    } //TODO: check size and format.

    public void setNationalCode(String nationalCode) {
        kycinfo.setLicenseid(nationalCode);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getCover() {
        return kycinfo.getUid() == null ? null : "http://coverishere.com";
    } //TODO: check how it work in upload master.

    public void setCover(String cover) {
        //TODO: check that how it work ?
    }

}
