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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "kycinfo")
@DynamicInsert
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Setter @Getter @NoArgsConstructor
public class Kycinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    /*UserId persist in AAS system*/
    @NotBlank//36 char
    private String uid;
    @NotNull
    private String fname;
    @NotNull
    private String lname;
    @NotNull
    private String licenseid;
    @NotNull
    private String country;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    private Status status = Status.pending;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @NotNull
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @NotNull
    private LicenseType ltype;

    //@CreationTimestamp
    @UpdateTimestamp
    @Column(name = "lastupdate", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Basic(optional = false)
    private Date lastupdate;


    public Kycinfo(Long id, String uid, String fname, String lname, String licenseid,
                   Gender gender, LicenseType licenseType,String country) {
        this.id = id;
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.licenseid = licenseid;
        this.gender = gender;
        this.ltype = licenseType;
        this.lastupdate= null;
        this.country = country;
    }

    /**
     * <b>toString</b> retun fields of kycInfo object
     * @return
     */
    public String toString()
    {
        String lastUpdateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(lastupdate);
        return String.format("KYC Information:[id=%d, UId=%s, FName=%s, LName=%s, Gender=%s, LicenseType='%s'," +
                " licenseId=%s , status=%s], LastUpdate=%s",id,uid,fname,lname,gender,ltype,licenseid,status,lastUpdateStr);
    }
}
