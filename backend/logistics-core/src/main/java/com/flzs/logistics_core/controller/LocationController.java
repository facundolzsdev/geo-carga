package com.flzs.logistics_core.controller;

import com.flzs.logistics_core.model.dto.georef.simple.DepartmentDTO;
import com.flzs.logistics_core.model.dto.georef.simple.MunicipalityDTO;
import com.flzs.logistics_core.model.dto.georef.simple.ProvinceDTO;
import com.flzs.logistics_core.service.GeoRefService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final GeoRefService geoRefService;

    @GetMapping("/provinces")
    public List<ProvinceDTO> getProvinces() {
        return geoRefService.getProvinces();
    }

    @GetMapping("/departments")
    public List<DepartmentDTO> getDepartments(@RequestParam String provinceID) {
        return geoRefService.getDepartmentsByProvince(provinceID);
    }

    @GetMapping("/municipalities")
    public List<MunicipalityDTO> getMunicipalities(@RequestParam String provinceID, String departmentID) {
        return geoRefService.getMunicipalitiesByProvinceAndDepartment(provinceID, departmentID);
    }
}
