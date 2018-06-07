package com.b2mark.kyc.entity;

import com.b2mark.kyc.enums.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "country")
@Getter @NoArgsConstructor
public class Country {
    @Id
    String id;
    @NotNull
    String name;

}
