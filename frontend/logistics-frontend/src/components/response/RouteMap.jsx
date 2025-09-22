import { useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

L.Marker.prototype.options.icon = DefaultIcon;

const originIcon = L.divIcon({
  html: `<div style="filter: hue-rotate(120deg) saturate(1.5) brightness(1.1);">
           <img src="${icon}" style="width: 25px; height: 41px;" />
         </div>`,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowUrl: iconShadow,
  shadowSize: [41, 41]
});

const destinationIcon = L.divIcon({
  html: `<div style="filter: hue-rotate(-30deg) saturate(1.5) brightness(1.1);">
           <img src="${icon}" style="width: 25px; height: 41px;" />
         </div>`,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowUrl: iconShadow,
  shadowSize: [41, 41]
});

// Component to handle map bounds fitting
function MapBounds({ routeCoordinates, originCoordinates, destinationCoordinates }) {
  const map = useRef();

  useEffect(() => {
    if (!map.current) return;

    const allCoords = [
      [originCoordinates[1], originCoordinates[0]], // Convert [lng, lat] to [lat, lng]
      [destinationCoordinates[1], destinationCoordinates[0]],
      ...routeCoordinates.map(coord => [coord[1], coord[0]]) // Convert route coords
    ];

    const bounds = L.latLngBounds(allCoords);
    map.current.fitBounds(bounds, { padding: [20, 20] });
  }, [routeCoordinates, originCoordinates, destinationCoordinates]);

  return null;
}

export function RouteMap({ mapData }) {
  if (!mapData || !mapData.coordenadasRuta || !mapData.coordenadasOrigen || !mapData.coordenadasDestino) {
    console.log('Map data validation failed:', {
      hasMapData: !!mapData,
      hasRoute: !!mapData?.coordenadasRuta,
      hasOrigin: !!mapData?.coordenadasOrigen,
      hasDestination: !!mapData?.coordenadasDestino
    });
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '400px' }}>
        <div className="text-center text-muted">
          <i className="bi bi-map" style={{ fontSize: '2rem' }}></i>
          <p className="mt-2 mb-0">No hay datos de mapa disponibles</p>
        </div>
      </div>
    );
  }

  // Convert coordinates from [lng, lat] to [lat, lng] 
  const routeCoords = mapData.coordenadasRuta.map(coord => [coord[1], coord[0]]);
  const originCoords = [mapData.coordenadasOrigen[1], mapData.coordenadasOrigen[0]];
  const destinationCoords = [mapData.coordenadasDestino[1], mapData.coordenadasDestino[0]];

  const centerLat = (originCoords[0] + destinationCoords[0]) / 2;
  const centerLng = (originCoords[1] + destinationCoords[1]) / 2;

  return (
    <div className="mt-4">
      <div className="card shadow-sm">
        <div className="card-header bg-ligth text-dark">
          <h6 className="mb-0">Mapa de la ruta</h6>
        </div>
        <div className="card-body p-0">
          <div style={{ height: '450px', width: '100%' }}>
            <MapContainer
              center={[centerLat, centerLng]}
              zoom={10}
              style={{ height: '100%', width: '100%' }}
              scrollWheelZoom={true}
              ref={map => { if (map) map.current = map; }}>
              <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

              {/* Origin Marker */}
              <Marker position={originCoords} icon={originIcon}>
                <Popup>
                  <div className="text-center">
                    <strong className="text-success">Origen</strong>
                    <br />
                    <small className="text-muted">
                      {originCoords[0].toFixed(4)}, {originCoords[1].toFixed(4)}
                    </small>
                  </div>
                </Popup>
              </Marker>

              {/* Destination Marker */}
              <Marker position={destinationCoords} icon={destinationIcon}>
                <Popup>
                  <div className="text-center">
                    <strong className="text-danger">Destino</strong>
                    <br />
                    <small className="text-muted">
                      {destinationCoords[0].toFixed(4)}, {destinationCoords[1].toFixed(4)}
                    </small>
                  </div>
                </Popup>
              </Marker>

              {/* Route Polyline */}
              <Polyline
                positions={routeCoords}
                pathOptions={{
                  color: '#007bff',
                  weight: 4,
                  opacity: 0.8,
                  dashArray: null
                }} />

              <MapBounds bounds={[originCoords, destinationCoords, ...routeCoords]} />
            </MapContainer>
          </div>
        </div>
      </div>
    </div>
  );
}