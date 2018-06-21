package com.b2mark.kyc.entity;

import lombok.*;
import javax.persistence.*;
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
