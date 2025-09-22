package com.flzs.logistics_core.model.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {
    private String streetNumber;
    private String street;
    private String municipality;
    private String department;
    private String province;
}