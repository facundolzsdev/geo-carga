package com.flzs.logistics_core.service;

import com.flzs.logistics_core.client.GeoRefClient;
import com.flzs.logistics_core.mapper.GeoRefMapper;
import com.flzs.logistics_core.model.dto.georef.simple.*;
import com.flzs.logistics_core.util.general.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GeoRefServiceImpl implements GeoRefService {

    private final GeoRefClient geoRefClient;
    private final GeoRefMapper geoRefMapper;

    @Override
    public List<ProvinceDTO> getProvinces() {
        var resp = geoRefClient.getProvinces();
        var provinces = geoRefMapper.toProvinceDTOList(resp.provinces());
        return SortUtils.sortBy(provinces, ProvinceDTO::name);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByProvince(String provinceID) {
        var resp = geoRefClient.getDepartmentsByProvince(provinceID);
        var departments = geoRefMapper.toDepartmentDTOList(resp.departments());
        return SortUtils.sortBy(departments, DepartmentDTO::name);
    }

    @Override
    public List<MunicipalityDTO> getMunicipalitiesByProvinceAndDepartment(String provinceID, String departmentID) {
        var resp = geoRefClient.getMunicipalitiesByProvinceAndDepartment(provinceID,departmentID);
        var municipalities = geoRefMapper.toMunicipalityDTOList(resp.municipalities());
        return SortUtils.sortBy(municipalities, MunicipalityDTO::name);
    }
}
