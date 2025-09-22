import { useState } from 'react';
import { RouteMap } from './RouteMap';

export function ShippingResult({ result }) {
  const [showAllInstructions, setShowAllInstructions] = useState(false);

  if (!result) return null;

  // Config for instruction limit
  const INITIAL_INSTRUCTIONS_LIMIT = 5;
  const hasMoreInstructions = result.instrucciones.length > INITIAL_INSTRUCTIONS_LIMIT;
  const instructionsToShow = showAllInstructions
    ? result.instrucciones
    : result.instrucciones.slice(0, INITIAL_INSTRUCTIONS_LIMIT);

  const toggleInstructions = () => {
    setShowAllInstructions(!showAllInstructions);
  };

  return (
    <div className="mt-4">
      {/* Main Results Card */}
      <div className="card shadow-sm border-0 mb-4">
        <div className="card-body">
          <h4 className="card-title text-center mb-4 fw-bold">¡Cotización Lista!</h4>

          <div className="row g-3 mb-4">
            {/* Price Card */}
            <div className="col-md-4">
              <div className="card bg-success text-white text-center h-100">
                <div className="card-body">
                  <h6 className="card-subtitle mb-1">Costo Total</h6>
                  <h4 className="card-title fw-bold">${result.precioEstimado}</h4>
                </div>
              </div>
            </div>

            {/* Time Card */}
            <div className="col-md-4">
              <div className="card bg-light text-dark text-center h-100">
                <div className="card-body">
                  <h6 className="card-subtitle mb-1">Tiempo Estimado</h6>
                  <h4 className="card-title fw-bold">{result.tiempoEstimadoHoras} hs</h4>
                </div>
              </div>
            </div>

            {/* Distance Card */}
            <div className="col-md-4">
              <div className="card bg-light text-dark text-center h-100">
                <div className="card-body">
                  <h6 className="card-subtitle mb-1">Distancia</h6>
                  <h4 className="card-title fw-bold">{result.distanciaKm} km</h4>
                </div>
              </div>
            </div>
          </div>

          {/* Instructions */}
          <div className="mb-4">
            <h5><i className="bi bi-signpost-split me-2"></i>Instrucciones de Ruta</h5>
            <div className="card">
              <div className="card-body p-0">
                <ol className="list-group list-group-numbered list-group-flush">
                  {instructionsToShow.map((inst, i) => (
                    <li key={i} className="list-group-item">
                      <small>{inst}</small>
                    </li>
                  ))}
                </ol>

                {hasMoreInstructions && (
                  <div className="text-center p-3 border-top">
                    <button
                      type="button"
                      className="btn btn-outline-dark btn-sm"
                      onClick={toggleInstructions}>
                      {showAllInstructions ? (
                        <>
                          <i className="bi bi-chevron-up me-1"></i>
                          Ver menos instrucciones
                        </>
                      ) : (
                        <>
                          <i className="bi bi-chevron-down me-1"></i>
                          Ver todas las instrucciones
                          ({result.instrucciones.length - INITIAL_INSTRUCTIONS_LIMIT} más)
                        </>
                      )}
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>

        </div>
      </div>

      {/* Map and Accuracy Messages */}
      <RouteMap mapData={result.datosMapa} />
      {(result.originAccuracyMessage || result.destinationAccuracyMessage) && (
        <div className="card border-0 mt-2">
          <div className="card-body p-3">
            <h6 className="card-title text-muted mb-2">
              <i className="bi bi-info-circle me-2"></i>Precisión de la ubicación
            </h6>

            {result.originAccuracyMessage && (
              <div className="d-flex align-items-start mb-2">
                <i className="bi bi-geo-alt text-success me-2 mt-1"></i>
                <div>
                  <small className="text-muted">{result.originAccuracyMessage}</small>
                </div>
              </div>
            )}

            {result.destinationAccuracyMessage && (
              <div className="d-flex align-items-start">
                <i className="bi bi-geo-alt text-primary me-2 mt-1"></i>
                <div>
                  <small className="text-muted">{result.destinationAccuracyMessage}</small>
                </div>
              </div>
            )}
          </div>
        </div>
      )}

    </div>
  );
}