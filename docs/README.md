## 馃搶 Descripci贸n del Proyecto

**GEO-CARGA** es una aplicaci贸n web fullstack (backend + frontend) para la cotizaci贸n de env铆os en Argentina.
Permite a los usuarios obtener una estimaci贸n completa que incluye costo, distancia en kil贸metros, tiempo de entrega aproximado y ruta en el mapa con instrucciones paso a paso.
El sistema integra fuentes de datos externas para garantizar precisi贸n en direcciones y trayectos, y presenta los resultados de manera clara mediante mapas interactivos y detalles del env铆o.

---

## 馃幆 Funcionalidades Principales

**La aplicaci贸n permite al usuario:**

1. **Ingresar direcci贸n de origen y destino:**
   - Selecci贸n guiada de provincias, departamentos y municipios.
   - Validaci贸n autom谩tica de direcciones para garantizar precisi贸n en el c谩lculo.

2. **Definir los datos del paquete:**
   - El usuario ingresa peso (kg) y dimensiones (cm鲁).
   - Validaci贸n autom谩tica en el frontend para evitar valores inv谩lidos o que excedan los l铆mites m谩ximos:
     * Peso m谩ximo: 2000 kg
     * Largo m谩ximo: 350 cm
     * Ancho m谩ximo: 175 cm
     * Alto m谩ximo: 150 cm

3. **Calcular cotizaci贸n de env铆o:**
   - El sistema env铆a los datos al backend y obtiene la distancia, tiempo estimado y costo del env铆o considerando peso y volumen.

4. **Visualizar la ruta en un mapa interactivo:**
   - Se muestra el trayecto completo entre origen y destino.
   - Marcadores diferenciados para origen y destino.
   - Posibilidad de hacer zoom y mover el mapa.

5. **Recibir mensajes claros en caso de error:**
   - Direcciones iguales, no encontradas o fuera de rango reciben un mensaje de validaci贸n amigable.
   - La aplicaci贸n evita c谩lculos inv谩lidos, asegurando consistencia y fiabilidad.

6. **Experiencia de usuario fluida:**
   - Carga din谩mica de provincias, departamentos y municipios.
   - Animaciones e indicaciones visuales para mantener al usuario informado durante el c谩lculo.

---

## 鈿欙笍 Tecnolog铆as Utilizadas

### 馃搼 Backend

- **Spring Boot 3.x** 鈫?Framework principal para crear el servidor y exponer endpoints REST.
- **WebClient** 鈫?Consumo de APIs externas (GeoRef y GeoApify).
- **Maven** 鈫?Gesti贸n de dependencias y construcci贸n del proyecto. 
- **MapStruct** 鈫?Mapeo autom谩tico entre DTOs y entidades.
- **Lombok** 鈫?Reducci贸n de boilerplate (getters, setters, constructores).
- **Java 17** 鈫?Lenguaje principal para la l贸gica de negocio.
- **SLF4J + Logback** 鈫?Sistema de logging para depuraci贸n y monitoreo.

### 馃搼 Frontend

- **Vite** 鈫?Herramienta de bundling y desarrollo r谩pido para React.
- **React** 鈫?Framework principal para la interfaz de usuario.
- **React Hooks** 鈫?Manejo de estado y l贸gica de componentes.
- **React-Bootstrap** 鈫?Componentes visuales predefinidos y estilizados.
- **React-Leaflet** 鈫?Integraci贸n de mapas interactivos en React.
- **Leaflet** 鈫?Biblioteca de mapas geogr谩ficos (OpenStreetMap como base).
- **Axios** 鈫?Comunicaci贸n HTTP con el backend.
- **JavaScript ES6+** 鈫?L贸gica del frontend y componentes din谩micos.

### 馃搼 Otros

- **OpenStreetMap** 鈫?Proveedor de teselas de mapa utilizadas en Leaflet.
- **APIs externas:**
  * **GeoRef** 鈫?Cat谩logo de provincias, departamentos y municipios argentinos.
  * **GeoApify** 鈫?Geocodificaci贸n, c谩lculo de ruta y distancia.

---

## 馃彈锔?Estructura General del Proyecto

### 馃搼 Backend

| Paquete      | Contenido                                                                                                                 | Descripci贸n                                                                                                 |
|--------------|---------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| `client`     | `GeoApifyClient`, `GeoRefClient`                                                                                          | Clases para consumir APIs externas y obtener datos de geocodificaci贸n o referencias geogr谩ficas.            |
| `controller` | `LocationController`, `ShipmentController`                                                                                | Expone endpoints REST para consultas de localizaci贸n y c谩lculo de env铆os.                                   |
| `exception`  | `ExternalApiException`, <br/> `ExternalApiUnavailableException`, `LocationNotFoundException`, `RouteCalculationException` | Definici贸n de excepciones personalizadas para manejo de errores espec铆ficos.                                |
| `mapper`     | `GeoRefMapper`                                                                                                            | Convierte entre DTOs y entidades del dominio.                                                               |
| `model`      | `domain`, `dto`, `enums`, `request`, `response`, `routing`                                                                | Contiene las clases de dominio, transfer objects, enumeraciones, requests/responses y estructuras de rutas. |
| `service`    | `GeoApifyService`, `GeoRefService`, `ShipmentService`                                                                     | Contiene la l贸gica de negocio y coordinaci贸n entre clientes, validaciones y c谩lculos.                       |
| `util`       | `builder`, `calculator`, `constants`, `general`, `handler`                                                                | Funciones y clases utilitarias generales para c谩lculos, construcci贸n de objetos y manejo de excepciones.    |

### 馃搼 Frontend

| M贸dulo       | Contenido                                                         | Descripci贸n                                                                                                                                |
|--------------|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `assets`     | `app-logo.png`                                                    | Imagen del logo de la App                                                                                                                  |
| `components` | `form`, `layout`, `response`                                      | Contiene los componentes de UI organizados por funcionalidad: formulario, layout general y presentaci贸n de resultados.                     |
| `hooks`      | `useApiRequest.js`, `useLocations.js`, `useShippingCalculator.js` | Manejan la l贸gica de negocio y comunicaci贸n con el backend, manteniendo los componentes simples.                                           |
| `pages`      | `HomePage.jsx`, `ResultsPage.jsx`                                 | P谩ginas principales que representan las vistas de la aplicaci贸n: formulario y resultados.                                                  |
| `services`   | `locationService.js`, `shippingService.js`                        | Encapsulan llamadas a APIs externas o al backend, proporcionando funciones reutilizables para obtener datos geogr谩ficos y calcular env铆os. |
| `utils`      | `api`, `constants`, `validators`                                  | Funciones y constantes auxiliares que soportan la gesti贸n de errores, rutas de la app y utilidades generales.                              |

---

## 馃攳 Desglose de Componentes Clave

### 馃搼 Backend

| Clase                    | Descripci贸n                                                                                                           |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `GeoApifyClient`         | Encapsula la comunicaci贸n con la API de **GeoApify**: geocodificaci贸n, c谩lculo de rutas y distancias.                 |
| `GeoRefClient`           | Consume la API de **GeoRef** para poblar listas de provincias, departamentos y municipios.                            |
| `LocationController`     | Controlador REST que expone los endpoints relacionados con direcciones y localidades.                                 |
| `ShipmentController`     | Controlador REST que maneja la creaci贸n de solicitudes de env铆o y c谩lculo de rutas.                                   |
| `GeoApifyService`        | L贸gica de negocio para geocodificaci贸n y c谩lculo de rutas usando `GeoApifyClient`.                                    |
| `GeoRefService`          | Gestiona datos de localidades usando `GeoRefClient`.                                                                  |
| `ShipmentService`        | Orquesta el flujo completo del c谩lculo de env铆os, validaci贸n de direcciones y preparaci贸n de la respuesta.            |
| `GlobalExceptionHandler` | Maneja todas las excepciones globales de la aplicaci贸n, transformando errores internos en respuestas REST coherentes. |


### 馃搼 Frontend

Se listan 煤nicamente los hooks, servicios y utilidades que centralizan la l贸gica y el manejo de errores. 
Los dem谩s componentes visuales no se desglosan ya que son autoexplicativos por nombre y estructura.
De todas formas, estos se incluyen en un apartado del documento [DEV_NOTES](./DEV_NOTES.md).

| Componente/Archivo                         | Descripci贸n                                                                    |
|--------------------------------------------|--------------------------------------------------------------------------------|
| `useLocations.js`                          | *Hook*. Carga provincias/departamentos/municipios. **Cachea provincias 24hs.** |
| `useShippingCalculator.js`                 | *Hook*. Env铆a datos al backend y decide mostrar resultado o error.             |
| `locationService.js`                       | *Servicio*. Solicita datos geogr谩ficos.                                        |
| `shippingService.js`                       | *Servicio*. Solicita cotizaci贸n de env铆o.                                      |
| `routes.js`                                | URLs de la app para reutilizaci贸n.                                             |
| `apiErrorHandler.js` + `errorConstants.js` | Traducen errores t茅cnicos en mensajes claros.                                  |

---

## 馃摝 Modelo de Cotizaci贸n
Para calcular el costo y el tiempo estimado de un env铆o, la aplicaci贸n utiliza un conjunto de par谩metros simb贸licos, 
definidos con base en valores de referencia actuales en Argentina.
Estos valores est谩n definidos en la clase `ShipmentParams` del backend y se utilizan en todos los c谩lculos de cotizaci贸n.

- Tiempo de entrega estimado seg煤n distancia:
  * Cortas distancias (menor que 100 km): 6 horas
  * Distancias medias (entre 100 y 500 km): 24 horas
  * Largas distancias (mayor que 500 km): 36 horas

- Par谩metros de precios:
  * Precio base: $1300
  * Costo por kil贸metro: $20/km
  * Costo por kilogramo: $75/kg
  * Costo por volumen: $0.01/cm鲁

Estos valores son simb贸licos y ajustados a modo demostrativo, no representan tarifas oficiales de ninguna empresa de log铆stica.

---

?? [Ver la playlist en YouTube](https://youtube.com/playlist?list=PLo6gJIiicJy_sBoJFaMlj9ZW8p6nWRiPT&si=PRa6qnuANEeMss5s)

---

馃搶 **Nota**: Para ver los detalles t茅cnicos internos, decisiones de implementaci贸n y apuntes de desarrollo, consulta las [notas de desarrollador](./DEV_NOTES.md).
