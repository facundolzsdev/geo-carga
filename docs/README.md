## 📌 Descripción del Proyecto

**GEO-CARGA** es una aplicación web fullstack (backend + frontend) para la cotización de envíos en Argentina.
Permite a los usuarios obtener una estimación completa que incluye costo, distancia en kilómetros, tiempo de entrega aproximado y ruta en el mapa con instrucciones paso a paso.
El sistema integra fuentes de datos externas para garantizar precisión en direcciones y trayectos, y presenta los resultados de manera clara mediante mapas interactivos y detalles del envío.

---

## 🎯 Funcionalidades Principales

**La aplicación permite al usuario:**

1. **Ingresar dirección de origen y destino:**
   - Selección guiada de provincias, departamentos y municipios.
   - Validación automática de direcciones para garantizar precisión en el cálculo.

2. **Definir los datos del paquete:**
   - El usuario ingresa peso (kg) y dimensiones (cm³).
   - Validación automática en el frontend para evitar valores inválidos o que excedan los límites máximos:
     * Peso máximo: 2000 kg
     * Largo máximo: 350 cm
     * Ancho máximo: 175 cm
     * Alto máximo: 150 cm

3. **Calcular cotización de envío:**
   - El sistema envía los datos al backend y obtiene la distancia, tiempo estimado y costo del envío considerando peso y volumen.

4. **Visualizar la ruta en un mapa interactivo:**
   - Se muestra el trayecto completo entre origen y destino.
   - Marcadores diferenciados para origen y destino.
   - Posibilidad de hacer zoom y mover el mapa.

5. **Recibir mensajes claros en caso de error:**
   - Direcciones iguales, no encontradas o fuera de rango reciben un mensaje de validación amigable.
   - La aplicación evita cálculos inválidos, asegurando consistencia y fiabilidad.

6. **Experiencia de usuario fluida:**
   - Carga dinámica de provincias, departamentos y municipios.
   - Animaciones e indicaciones visuales para mantener al usuario informado durante el cálculo.

---

## ⚙️ Tecnologías Utilizadas

### 📑 Backend

- **Spring Boot 3.x** �?Framework principal para crear el servidor y exponer endpoints REST.
- **WebClient** �?Consumo de APIs externas (GeoRef y GeoApify).
- **Maven** �?Gestión de dependencias y construcción del proyecto. 
- **MapStruct** �?Mapeo automático entre DTOs y entidades.
- **Lombok** �?Reducción de boilerplate (getters, setters, constructores).
- **Java 17** �?Lenguaje principal para la lógica de negocio.
- **SLF4J + Logback** �?Sistema de logging para depuración y monitoreo.

### 📑 Frontend

- **Vite** �?Herramienta de bundling y desarrollo rápido para React.
- **React** �?Framework principal para la interfaz de usuario.
- **React Hooks** �?Manejo de estado y lógica de componentes.
- **React-Bootstrap** �?Componentes visuales predefinidos y estilizados.
- **React-Leaflet** �?Integración de mapas interactivos en React.
- **Leaflet** �?Biblioteca de mapas geográficos (OpenStreetMap como base).
- **Axios** �?Comunicación HTTP con el backend.
- **JavaScript ES6+** �?Lógica del frontend y componentes dinámicos.

### 📑 Otros

- **OpenStreetMap** �?Proveedor de teselas de mapa utilizadas en Leaflet.
- **APIs externas:**
  * **GeoRef** �?Catálogo de provincias, departamentos y municipios argentinos.
  * **GeoApify** �?Geocodificación, cálculo de ruta y distancia.

---

## 🏗�?Estructura General del Proyecto

### 📑 Backend

| Paquete      | Contenido                                                                                                                 | Descripción                                                                                                 |
|--------------|---------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| `client`     | `GeoApifyClient`, `GeoRefClient`                                                                                          | Clases para consumir APIs externas y obtener datos de geocodificación o referencias geográficas.            |
| `controller` | `LocationController`, `ShipmentController`                                                                                | Expone endpoints REST para consultas de localización y cálculo de envíos.                                   |
| `exception`  | `ExternalApiException`, <br/> `ExternalApiUnavailableException`, `LocationNotFoundException`, `RouteCalculationException` | Definición de excepciones personalizadas para manejo de errores específicos.                                |
| `mapper`     | `GeoRefMapper`                                                                                                            | Convierte entre DTOs y entidades del dominio.                                                               |
| `model`      | `domain`, `dto`, `enums`, `request`, `response`, `routing`                                                                | Contiene las clases de dominio, transfer objects, enumeraciones, requests/responses y estructuras de rutas. |
| `service`    | `GeoApifyService`, `GeoRefService`, `ShipmentService`                                                                     | Contiene la lógica de negocio y coordinación entre clientes, validaciones y cálculos.                       |
| `util`       | `builder`, `calculator`, `constants`, `general`, `handler`                                                                | Funciones y clases utilitarias generales para cálculos, construcción de objetos y manejo de excepciones.    |

### 📑 Frontend

| Módulo       | Contenido                                                         | Descripción                                                                                                                                |
|--------------|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `assets`     | `app-logo.png`                                                    | Imagen del logo de la App                                                                                                                  |
| `components` | `form`, `layout`, `response`                                      | Contiene los componentes de UI organizados por funcionalidad: formulario, layout general y presentación de resultados.                     |
| `hooks`      | `useApiRequest.js`, `useLocations.js`, `useShippingCalculator.js` | Manejan la lógica de negocio y comunicación con el backend, manteniendo los componentes simples.                                           |
| `pages`      | `HomePage.jsx`, `ResultsPage.jsx`                                 | Páginas principales que representan las vistas de la aplicación: formulario y resultados.                                                  |
| `services`   | `locationService.js`, `shippingService.js`                        | Encapsulan llamadas a APIs externas o al backend, proporcionando funciones reutilizables para obtener datos geográficos y calcular envíos. |
| `utils`      | `api`, `constants`, `validators`                                  | Funciones y constantes auxiliares que soportan la gestión de errores, rutas de la app y utilidades generales.                              |

---

## 🔍 Desglose de Componentes Clave

### 📑 Backend

| Clase                    | Descripción                                                                                                           |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `GeoApifyClient`         | Encapsula la comunicación con la API de **GeoApify**: geocodificación, cálculo de rutas y distancias.                 |
| `GeoRefClient`           | Consume la API de **GeoRef** para poblar listas de provincias, departamentos y municipios.                            |
| `LocationController`     | Controlador REST que expone los endpoints relacionados con direcciones y localidades.                                 |
| `ShipmentController`     | Controlador REST que maneja la creación de solicitudes de envío y cálculo de rutas.                                   |
| `GeoApifyService`        | Lógica de negocio para geocodificación y cálculo de rutas usando `GeoApifyClient`.                                    |
| `GeoRefService`          | Gestiona datos de localidades usando `GeoRefClient`.                                                                  |
| `ShipmentService`        | Orquesta el flujo completo del cálculo de envíos, validación de direcciones y preparación de la respuesta.            |
| `GlobalExceptionHandler` | Maneja todas las excepciones globales de la aplicación, transformando errores internos en respuestas REST coherentes. |


### 📑 Frontend

Se listan únicamente los hooks, servicios y utilidades que centralizan la lógica y el manejo de errores. 
Los demás componentes visuales no se desglosan ya que son autoexplicativos por nombre y estructura.
De todas formas, estos se incluyen en un apartado del documento [DEV_NOTES](./DEV_NOTES.md).

| Componente/Archivo                         | Descripción                                                                    |
|--------------------------------------------|--------------------------------------------------------------------------------|
| `useLocations.js`                          | *Hook*. Carga provincias/departamentos/municipios. **Cachea provincias 24hs.** |
| `useShippingCalculator.js`                 | *Hook*. Envía datos al backend y decide mostrar resultado o error.             |
| `locationService.js`                       | *Servicio*. Solicita datos geográficos.                                        |
| `shippingService.js`                       | *Servicio*. Solicita cotización de envío.                                      |
| `routes.js`                                | URLs de la app para reutilización.                                             |
| `apiErrorHandler.js` + `errorConstants.js` | Traducen errores técnicos en mensajes claros.                                  |

---

## 📦 Modelo de Cotización
Para calcular el costo y el tiempo estimado de un envío, la aplicación utiliza un conjunto de parámetros simbólicos, 
definidos con base en valores de referencia actuales en Argentina.
Estos valores están definidos en la clase `ShipmentParams` del backend y se utilizan en todos los cálculos de cotización.

- Tiempo de entrega estimado según distancia:
  * Cortas distancias (menor que 100 km): 6 horas
  * Distancias medias (entre 100 y 500 km): 24 horas
  * Largas distancias (mayor que 500 km): 36 horas

- Parámetros de precios:
  * Precio base: $1300
  * Costo por kilómetro: $20/km
  * Costo por kilogramo: $75/kg
  * Costo por volumen: $0.01/cm³

Estos valores son simbólicos y ajustados a modo demostrativo, no representan tarifas oficiales de ninguna empresa de logística.

---

?? [Ver la playlist en YouTube](https://youtube.com/playlist?list=PLo6gJIiicJy_sBoJFaMlj9ZW8p6nWRiPT&si=PRa6qnuANEeMss5s)

---

📌 **Nota**: Para ver los detalles técnicos internos, decisiones de implementación y apuntes de desarrollo, consulta las [notas de desarrollador](./DEV_NOTES.md).
