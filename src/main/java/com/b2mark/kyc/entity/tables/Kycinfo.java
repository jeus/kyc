/**
 * <h1>kycinfo entity class</h1>
 * kycinfo persist user kyc information and status of validation that.
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */
package com.b2mark.kyc.entity.tables;

import com.b2mark.kyc.enums.Gender;
import com.b2mark.kyc.enums.Status;
import com.b2mark.kyc.enums.LicenseType;
import com.b2mark.kyc.enums.PostgreSQLEnumType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    /*UserId persist in AAS system*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty( readOnly = true)
    private Long id ;
    @NotBlank//36 char
    @ApiModelProperty(readOnly = true)
    private String uid;
    @NotNull
    @Size(max=20)
    @Size(min = 2)
    private String fname;
    @NotNull
    @Size(max=20)
    @Size(min = 2)
    private String lname;
    @NotNull
    private String licenseid;
    @NotNull
    @Size(min = 2,max = 3)
    private String country;
    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @ApiModelProperty(readOnly = true)
    private Status status = Status.pending;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @NotNull
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    @NotNull
    private LicenseType ltype;


    @UpdateTimestamp
    @ApiModelProperty(readOnly = true)
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
