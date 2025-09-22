package com.flzs.logistics_core.service;

import com.flzs.logistics_core.model.dto.georef.simple.*;

import java.util.List;

public interface GeoRefService {

    List<ProvinceDTO> getProvinces();

    List<DepartmentDTO> getDepartmentsByProvince(String provinceID);

    List<MunicipalityDTO> getMunicipalitiesByProvinceAndDepartment(String provinceID, String departmentID);
}
