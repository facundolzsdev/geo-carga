export function PackageSection({ data, errors, onChange }) {
    const fields = [
        { name: "weightKg", label: "Peso", icon: "bi-speedometer2", type: "number", max: "2,000", placeholder: "0.0", step: "0.1" },
        { name: "heightCm", label: "Alto", icon: "bi-arrows-vertical", type: "number", max: "150", placeholder: "0" },
        { name: "widthCm", label: "Ancho", icon: "bi-arrows-horizontal", type: "number", max: "175", placeholder: "0" },
        { name: "lengthCm", label: "Largo", icon: "bi-arrows-fullscreen", type: "number", max: "350", placeholder: "0" }
    ];

    return (
        <div className="card mb-4 border-0 shadow-sm">
            <div className="card-body">
                <h5 className="card-title">Detalles del paquete</h5>
                <div className="row">
                    {fields.map((field) => (
                        <div className="col-md-6 mb-3" key={field.name}>
                            <label className="form-label">
                                <i className={`${field.icon} me-1`}></i>{field.label} <small className="text-muted">(máx. {field.max})</small>
                            </label>
                            <input
                                type={field.type}
                                step={field.step}
                                className={`form-control ${errors[`packageData.${field.name}`] ? 'is-invalid' : ''}`}
                                placeholder={field.placeholder}
                                value={data[field.name]}
                                onChange={(e) => onChange({ ...data, [field.name]: e.target.value })} />
                            {errors[`packageData.${field.name}`] && (
                                <div className="invalid-feedback d-block">{errors[`packageData.${field.name}`]}</div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}