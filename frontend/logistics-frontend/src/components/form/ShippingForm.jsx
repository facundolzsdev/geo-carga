import { useState, useCallback, useMemo } from "react";
import { validateShipmentData } from "../../utils/validators/shipmentFormValidator";
import { AddressSection } from "./AddressSection";
import { PackageSection } from "./PackageSection";

export function ShippingForm({ onSubmit, isLoading }) {
  const [formData, setFormData] = useState({
    origin: {
      streetNumber: "",
      street: "",
      municipality: null,
      province: null,
      department: null
    },
    destination: {
      streetNumber: "",
      street: "",
      municipality: null,
      province: null,
      department: null
    },
    packageData: {
      weightKg: "",
      heightCm: "",
      widthCm: "",
      lengthCm: ""
    }
  });

  const [errors, setErrors] = useState({});

  const validateForm = useCallback(() => {
    const validationErrors = validateShipmentData(formData);
    return {
      isValid: Object.keys(validationErrors).length === 0,
      errors: validationErrors
    };
  }, [formData]);

  const { isValid: isFormValid } = useMemo(validateForm, [validateForm]);

  const handleOriginChange = useCallback((newOrigin) => {
    setFormData(prev => ({ ...prev, origin: newOrigin }));
  }, []);

  const handleDestinationChange = useCallback((newDestination) => {
    setFormData(prev => ({ ...prev, destination: newDestination }));
  }, []);

  const handlePackageChange = useCallback((newPackageData) => {
    setFormData(prev => ({ ...prev, packageData: newPackageData }));
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isLoading) return;

    const { isValid, errors } = validateForm();
    if (!isValid) {
      setErrors(errors);
      return;
    }

    // Transform objects to strings 
    const transformAddress = (address) => ({
      streetNumber: address.streetNumber,
      street: address.street,
      municipality: typeof address.municipality === 'object' ? address.municipality.name : address.municipality,
      department: typeof address.department === 'object' ? address.department.name : address.department,
      province: typeof address.province === 'object' ? address.province.name : address.province
    });

    setErrors({});
    onSubmit({
      origen: transformAddress(formData.origin),
      destino: transformAddress(formData.destination),
      paquete: {
        ...formData.packageData,
        weightKg: parseFloat(formData.packageData.weightKg),
        heightCm: parseFloat(formData.packageData.heightCm),
        widthCm: parseFloat(formData.packageData.widthCm),
        lengthCm: parseFloat(formData.packageData.lengthCm),
      },
    });
  };

  return (
    <form onSubmit={handleSubmit} className="container-md mx-auto p-4 bg-light rounded shadow-sm"
      style={{ maxWidth: '900px' }}>
      <AddressSection
        title="Origen"
        data={formData.origin}
        errors={errors}
        prefix="origin"
        onChange={handleOriginChange} />

      <AddressSection
        title="Destino"
        data={formData.destination}
        errors={errors}
        prefix="destination"
        onChange={handleDestinationChange} />

      <PackageSection
        data={formData.packageData}
        errors={errors}
        onChange={handlePackageChange} />

      <div className="d-grid">
        <button
          type="submit"
          className="btn btn-dark btn-lg"
          disabled={!isFormValid || isLoading}
          title={!isFormValid ? "Complete todos los campos requeridos" : ""}>
          {isLoading ? (
            <>
              <span className="spinner-border spinner-border-sm me-2" role="status"></span>
              Cotizando...
            </>
          ) : (
            "¡Cotizar Envío!"
          )}
        </button>
      </div>
    </form>
  );
}