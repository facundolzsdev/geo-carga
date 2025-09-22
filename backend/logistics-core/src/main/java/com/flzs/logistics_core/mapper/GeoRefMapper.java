package com.flzs.logistics_core.mapper;

import com.flzs.logistics_core.model.dto.georef.raw.*;
import com.flzs.logistics_core.model.dto.georef.simple.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeoRefMapper {

    @Mapping(source = "name", target = "name")
    ProvinceDTO toProvinceDTO(GeoRefProvinceResponse.Province province);

    List<ProvinceDTO> toProvinceDTOList(List<GeoRefProvinceResponse.Province> provinces);

    @Mapping(source = "province.id", target = "provinceID")
    DepartmentDTO toDepartmentDTO(GeoRefDepartmentResponse.Department department);

    List<DepartmentDTO> toDepartmentDTOList(List<GeoRefDepartmentResponse.Department> departments);

    @Mapping(source = "province.id", target = "provinceID")
    @Mapping(source = "department.id", target = "departmentID")
    MunicipalityDTO toMunicipalityDTO(GeoRefMunicipalityResponse.Municipality municipality);

    List<MunicipalityDTO> toMunicipalityDTOList(List<GeoRefMunicipalityResponse.Municipality> municipalities);

}
