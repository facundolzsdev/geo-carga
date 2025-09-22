import { useState } from 'react';
import { Dropdown } from 'react-bootstrap';
import { useLocations } from '../../hooks/useLocations';

export function AddressSection({ title, data, errors, prefix, onChange }) {
  const {
    provinces,
    departments,
    municipalities,
    loadingProvinces,
    loadingDepartments,
    loadingMunicipalities,
    provincesError,
    departmentsError,
    municipalitiesError,
    setSelectedProvince,
    setSelectedDepartment
  } = useLocations(data.province, data.department);

  const [showProvince, setShowProvince] = useState(false);
  const [showDepartment, setShowDepartment] = useState(false);
  const [showMunicipality, setShowMunicipality] = useState(false);

  const handleChange = (name, value) => {
    let processedValue = value;

    // Parse JSON values for province, department, municipality
    if (['province', 'department', 'municipality'].includes(name) && value) {
      try {
        processedValue = JSON.parse(value);
      } catch {
        processedValue = value; // fallback if not JSON
      }
    }

    if (name === 'province') {
      setSelectedProvince(processedValue?.id || processedValue);
      // Reset department and municipality when province changes
      onChange({ ...data, [name]: processedValue, department: null, municipality: null });
    } else if (name === 'department') {
      setSelectedDepartment(processedValue?.id || processedValue);
      // Reset municipality when department changes
      onChange({ ...data, [name]: processedValue, municipality: null });
    } else {
      onChange({ ...data, [name]: processedValue });
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    onChange({ ...data, [name]: value });
  };

  const errorMessage =
    provincesError?.toString() ||
    departmentsError?.toString() ||
    municipalitiesError?.toString();

  const provincePlaceholder = loadingProvinces
    ? "Cargando provincias..."
    : provincesError
      ? "Error al cargar provincias"
      : "Seleccione provincia";

  const departmentPlaceholder = !data.province
    ? "Seleccione provincia primero"
    : loadingDepartments
      ? "Cargando departamentos..."
      : departmentsError
        ? "Error al cargar departamentos"
        : "Seleccione departamento";

  const isLoadingMunicipalities = data.department && loadingMunicipalities;
  const hasMunicipalities = data.department && !isLoadingMunicipalities && municipalities && municipalities.length > 0;

  let municipalityDisplayValue;
  if (data.municipality) {
    municipalityDisplayValue = data.municipality.name;
  } else if (!data.department) {
    municipalityDisplayValue = "Seleccione dpto primero";
  } else if (isLoadingMunicipalities) {
    municipalityDisplayValue = "Cargando municipios...";
  } else if (municipalitiesError) {
    municipalityDisplayValue = "Error al cargar municipios";
  } else {
    municipalityDisplayValue = "Seleccione municipio";
  }

  return (
    <div className="card mb-4 border-0 shadow-sm">
      <div className="card-body">
        <h5 className="card-title">{title}</h5>

        {errorMessage && !errors[`${prefix}.province`] && !errors[`${prefix}.department`]
          && !errors[`${prefix}.municipality`]
          && (<div className="alert alert-warning mb-3">
            <i className="bi bi-exclamation-triangle me-2"></i>
            {errorMessage}
          </div>)}

        <div className="row">
          {/* Select Province */}
          <div className="col-12 col-md-4 mb-3">
            <label className="form-label"><i className="bi bi-geo-alt me-1"></i>Provincia</label>
            <div className={`input-group-custom ${loadingProvinces ? 'loading' : ''} ${errors[`${prefix}.province`] ? 'is-invalid' : ''}`}>
              <Dropdown
                show={showProvince}
                onToggle={(isOpen) => setShowProvince(isOpen)}
                className="w-100">
                <Dropdown.Toggle
                  variant="outline-secondary"
                  id="dropdown-province"
                  className="custom-dropdown"
                  disabled={loadingProvinces}>
                  <span className="truncate-text" title={data.province ? data.province.name : provincePlaceholder}>
                    {data.province ? data.province.name : provincePlaceholder}
                  </span>
                </Dropdown.Toggle>
                <Dropdown.Menu className="custom-dropdown-menu w-100">
                  {!loadingProvinces && provinces.map((province) => (
                    <Dropdown.Item
                      key={province.id}
                      onClick={() => handleChange('province', JSON.stringify({ id: province.id, name: province.name }))}
                      title={province.name}
                      className="text-wrap">
                      {province.name}
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
              {loadingProvinces && (
                <span className="input-group-text">
                  <span className="spinner-border spinner-border-sm" aria-hidden="true"></span>
                </span>
              )}
            </div>
            {errors[`${prefix}.province`] && (
              <div className="invalid-feedback d-block">{errors[`${prefix}.province`]}</div>
            )}
          </div>

          {/* Select Department */}
          <div className="col-12 col-md-4 mb-3">
            <label className="form-label"><i className="bi bi-pin-map me-1"></i>Departamento</label>
            <div className={`input-group-custom ${loadingDepartments ? 'loading' : ''} ${errors[`${prefix}.department`] ? 'is-invalid' : ''}`}>
              <Dropdown
                show={showDepartment}
                onToggle={(isOpen) => setShowDepartment(isOpen)}
                className="w-100">
                <Dropdown.Toggle
                  variant="outline-secondary"
                  id="dropdown-department"
                  className="custom-dropdown"
                  disabled={!data.province || loadingDepartments}>
                  <span className="truncate-text" title={data.department ? data.department.name : departmentPlaceholder}>
                    {data.department ? data.department.name : departmentPlaceholder}
                  </span>
                </Dropdown.Toggle>
                <Dropdown.Menu className="custom-dropdown-menu w-100">
                  {!loadingDepartments && departments.map((department) => (
                    <Dropdown.Item
                      key={department.id}
                      onClick={() => handleChange('department', JSON.stringify({ id: department.id, name: department.name }))}
                      title={department.name}
                      className="text-wrap">
                      {department.name}
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
              {loadingDepartments && (
                <span className="input-group-text">
                  <span className="spinner-border spinner-border-sm" aria-hidden="true"></span>
                </span>
              )}
            </div>
            {errors[`${prefix}.department`] && (
              <div className="invalid-feedback d-block">{errors[`${prefix}.department`]}</div>
            )}
          </div>

          {/* Select Municipality */}
          <div className="col-12 col-md-4 mb-3">
            <label className="form-label"><i className="bi bi-building me-1"></i>Municipio</label>
            <div className={`input-group-custom ${loadingMunicipalities ? 'loading' : ''} ${errors[`${prefix}.municipality`] ? 'is-invalid' : ''}`}>
              <Dropdown
                show={showMunicipality}
                onToggle={(isOpen) => setShowMunicipality(isOpen)}
                className="w-100">
                <Dropdown.Toggle
                  variant="outline-secondary"
                  id="dropdown-municipality"
                  className="custom-dropdown"
                  disabled={!data.department || isLoadingMunicipalities}>
                  <span className="truncate-text" title={municipalityDisplayValue}>
                    {municipalityDisplayValue}
                  </span>
                </Dropdown.Toggle>
                <Dropdown.Menu className="custom-dropdown-menu w-100">
                  {isLoadingMunicipalities ? (
                    <Dropdown.Item disabled>
                      <div className="text-center py-2">
                        <span className="spinner-border spinner-border-sm me-2" aria-hidden="true"></span>
                        Cargando municipios...
                      </div>
                    </Dropdown.Item>
                  ) : hasMunicipalities ? (
                    municipalities.map((municipality) => (
                      <Dropdown.Item
                        key={municipality.id}
                        onClick={() => handleChange('municipality', JSON.stringify({ id: municipality.id, name: municipality.name }))}
                        title={municipality.name}
                        className="text-wrap">
                        {municipality.name}
                      </Dropdown.Item>
                    ))
                  ) : data.department ? (
                    <Dropdown.Item disabled>
                      <div className="text-center py-2">
                        <i className="bi bi-info-circle me-2"></i>
                        No hay municipios disponibles
                      </div>
                    </Dropdown.Item>
                  ) : (
                    <Dropdown.Item disabled>
                      <div className="text-center py-2">
                        Seleccione un dpto primero
                      </div>
                    </Dropdown.Item>
                  )}
                </Dropdown.Menu>
              </Dropdown>
              {loadingMunicipalities && (
                <span className="input-group-text">
                  <span className="spinner-border spinner-border-sm" aria-hidden="true"></span>
                </span>
              )}
            </div>
            {errors[`${prefix}.municipality`] && (
              <div className="invalid-feedback d-block">{errors[`${prefix}.municipality`]}</div>
            )}
          </div>
        </div>

        {/* Street and Number fields */}
        <div className="row">
          <div className="col-12 col-md-8 mb-3">
            <label className="form-label"><i className="bi bi-signpost me-1"></i>Calle</label>
            <input
              name="street"
              className={`form-control ${errors[`${prefix}.street`] ? 'is-invalid' : ''}`}
              placeholder="Ej: Av. Corrientes"
              value={data.street || ''}
              onChange={handleInputChange} />
            {errors[`${prefix}.street`] && (
              <div className="invalid-feedback d-block">{errors[`${prefix}.street`]}</div>
            )}
          </div>

          <div className="col-12 col-md-4 mb-3">
            <label className="form-label"><i className="bi bi-123 me-1"></i>Altura</label>
            <input
              type="number"
              name="streetNumber"
              className={`form-control ${errors[`${prefix}.streetNumber`] ? 'is-invalid' : ''}`}
              placeholder="Ej: 1234"
              value={data.streetNumber || ''}
              onChange={handleInputChange}
              min="1" />
            {errors[`${prefix}.streetNumber`] && (
              <div className="invalid-feedback d-block">{errors[`${prefix}.streetNumber`]}</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}