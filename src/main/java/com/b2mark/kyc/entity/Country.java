package com.b2mark.kyc.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "country")
@Getter @NoArgsConstructor @AllArgsConstructor
public class Country {
    @Id
    @ApiModelProperty(readOnly = true)
    String id;
    @NotNull
    @ApiModelProperty(readOnly = true)
    String name;
}
