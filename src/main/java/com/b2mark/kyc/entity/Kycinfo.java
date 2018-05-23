package com.b2mark.kyc.entity;

import javax.persistence.*;

@Entity
public class Kycinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    /*UserId persist in AAS system*/
    private Integer uid;
    private String fname;
    private String lname;
    private String licenseid;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;


    public Kycinfo(Long id, Integer uid, String fname, String lname, String licenseid, Gender gender, LicenseType licenseType) {
        this.id = id;
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.licenseid = licenseid;
        this.gender = gender;
        this.licenseType = licenseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public String toString()
    {
        return String.format("KYC Information:[id=%d, UId=%s, FName=%s, LName=%s, Gender=%s, LicenseType='%s'," +
                " licenseId=%s]",id,uid,fname,lname,gender,licenseType,licenseid);
    }
}
