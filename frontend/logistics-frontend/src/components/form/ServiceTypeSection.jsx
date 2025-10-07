const SERVICE_TYPES = [
    {
        id: 'normal',
        name: 'Normal',
        description: 'Más económico, entrega estándar',
        icon: 'bi-clock',
        multiplier: 1.0,
        timeReduction: 0
    },
    {
        id: 'express',
        name: 'Express',
        description: 'Entrega más rápida',
        icon: 'bi-lightning',
        multiplier: 1.25,
        timeReduction: 20
    },
    {
        id: 'prioritario',
        name: 'Prioritario',
        description: 'Entrega premium',
        icon: 'bi-star',
        multiplier: 1.5,
        timeReduction: 40
    }
];

export function ServiceTypeSection({ selectedType = 'normal', onChange }) {
    return (
        <div className="card mb-4 border-0 shadow-sm">
            <div className="card-body">
                <h5 className="card-title">
                    <i className="bi bi-truck me-2"></i>Tipo de Servicio
                </h5>
                <p className="text-muted mb-3">Selecciona la modalidad de envío</p>

                <div className="row g-3">
                    {SERVICE_TYPES.map((service) => (
                        <div className="col-12 col-md-4" key={service.id}>
                            <input
                                type="radio"
                                className="btn-check"
                                id={`service-${service.id}`}
                                name="serviceType"
                                value={service.id}
                                checked={selectedType === service.id}
                                onChange={(e) => onChange(e.target.value)}
                            />
                            <label
                                className={`card w-100 text-start p-3 h-100 border-2 ${selectedType === service.id
                                        ? 'border-success shadow-sm'
                                        : 'border-light'
                                    }`}
                                htmlFor={`service-${service.id}`}
                                style={{
                                    cursor: 'pointer',
                                    transition: 'all 0.2s ease-in-out'
                                }}
                            >
                                <div className="d-flex align-items-start">
                                    <i className={`${service.icon} fs-4 me-3 ${selectedType === service.id ? 'text-success' : 'text-muted'
                                        }`}></i>
                                    <div className="flex-grow-1">
                                        <h6 className={`mb-1 ${selectedType === service.id ? 'text-success fw-bold' : ''
                                            }`}>
                                            {service.name}
                                            {selectedType === service.id && (
                                                <i className="bi bi-check-circle-fill text-success ms-2"></i>
                                            )}
                                        </h6>
                                        <small className="text-muted d-block mb-2">
                                            {service.description}
                                        </small>
                                        <div className="mt-2">
                                            <small className={`badge me-1 ${selectedType === service.id ? 'bg-success' : 'bg-secondary'
                                                }`}>
                                                <i className="bi bi-currency-dollar me-1"></i>
                                                x{service.multiplier}
                                            </small>
                                            {service.timeReduction > 0 && (
                                                <small className={`badge ${selectedType === service.id ? 'bg-success' : 'bg-secondary'
                                                    }`}>
                                                    <i className="bi bi-clock me-1"></i>
                                                    -{service.timeReduction}% tiempo
                                                </small>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}