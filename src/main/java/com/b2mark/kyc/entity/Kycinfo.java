/**
 * <h1>kycinfo entity class</h1>
 * kycinfo persist user kyc information and status of validation that.
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */
package com.b2mark.kyc.entity;

import com.b2mark.kyc.enums.Gender;
import com.b2mark.kyc.enums.Status;
import com.b2mark.kyc.enums.LicenseType;
import com.b2mark.kyc.enums.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "kycinfo")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Kycinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id ;
    /*UserId persist in AAS system*/
    @NotBlank
    private Integer uid;
    @NotNull
    private String fname;
    @NotNull
    private String lname;
    @NotNull
    private String licenseid;


    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    private Status status = Status.not_active;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @NotNull
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @NotNull
    private LicenseType ltype;

    @Column(name = "lastupdate", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastupdate;

    /**
     * this class using by JPA.
     */
    public Kycinfo()
    {}


    public Kycinfo(Long id, Integer uid, String fname, String lname, String licenseid,
                   Gender gender, LicenseType licenseType) {
        this.id = id;
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.licenseid = licenseid;
        this.gender = gender;
        this.ltype = licenseType;
        this.lastupdate= null;
    }

    public Date getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LicenseType getLtype() {
        return ltype;
    }

    public void setLtype(LicenseType ltype) {
        this.ltype = ltype;
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

    /**
     * <b>toString</b> retun fields of kycInfo object
     * @return
     */
    public String toString()
    {
        String lastUpdateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(lastupdate);
        return String.format("KYC Information:[id=%d, UId=%s, FName=%s, LName=%s, Gender=%s, LicenseType='%s'," +
                " licenseId=%s], LastUpdate=%s",id,uid,fname,lname,gender,ltype,licenseid,lastUpdateStr);
    }
}
