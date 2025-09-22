package com.flzs.logistics_core.model.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PackageDetails {
    private double weightKg;
    private double heightCm;
    private double widthCm;
    private double lengthCm;
}