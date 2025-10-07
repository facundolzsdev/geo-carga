# Proyecto GEO-CARGA: Cotizar Envíos 💲🚚📦

El presente documento recopila notas que describen decisiones técnicas, flujos de trabajo y reflexiones sobre el desarrollo del proyecto.

---

## 📌 Anotaciones Backend

### 📡 Responsabilidades de las APIs externas

* **GeoRef:** **catálogo** para poblar listas desplegables (provincias, departamentos, municipios).
* **GeoApify:** **geocodificación** (coordenadas), cálculo de ruta, distancia e instrucciones.

---

### ✅ Validación de Direcciones

Para trabajar con datos precisos y evitar fallos, **las direcciones (origen y destino) se validan antes de calcular**.

* Una dirección se acepta si la **diferencia con lo devuelto por GeoApify es ≤ 2 km**.
* Si no cumple, la app **rechaza la dirección y muestra un mensaje al usuario**.

En caso de aceptación, se informa también la **proximidad (en metros)** entre lo ingresado y lo hallado, ya que **GeoApify** suele devolver un **punto cercano, no exacto**.

La precisión se marca como **aproximado** o **fallback**, usando `rank.confidence`, `rank.match_type` y `result_type`.
**GeoApify** siempre devuelve *approximate* en `match_type`, incluso cuando coincide con la dirección ingresada (ej. distinta altura). Por eso se fija un rango razonable (**2 km**).

Se crearon métodos separados para origen y destino:

* `resolveOriginAddress(...)`
* `resolveDestinationAddress(...)`

Ambos delegan en `geocodeAddressWithProximityValidation(...)`, pero se separaron pensando en posibles parámetros distintos en el futuro.

---

### ❌ Municipio No Encontrado

Si un municipio no está en **GeoApify**, se informa al usuario con un **mensaje claro**. **GeoRef** suele **tener localidades argentinas pequeñas**, mientras que **GeoApify**, al ser global, **puede no tenerlas**.

Para esto, uso `LocationNotFoundException`, incluyendo provincia y municipio. No creé una excepción exclusiva para no saturar el código.

---

### 🌲 Jerarquía de Excepciones

`ExternalApiException` es la excepción base, **nunca se lanza directamente**. Contiene el comportamiento común para todos los errores de API externos.

Las siguientes tres excepciones específicas extienden de esta y son las que realmente se lanzan:

* `LocationNotFoundException`
* `RouteCalculationException`
* `ExternalApiUnavailableException`

Con este enfoque, busco un **manejo polimórfico y granular de excepciones**.

Por ejemplo:

```java
try {
  //...lógica de geocodificación...
} catch (LocationNotFoundException e) {
  //Manejo específico para problemas de ubicación
} catch (RouteCalculationException e) {
  //Manejo específico para problemas de enrutamiento
} catch (ExternalApiException e) {
  //Opción de reserva para cualquier otro problema de API externo no manejado específicamente
}
```

También se logra un **mapeo de excepciones limpio** en la clase `GlobalExceptionHandler`. 

Por ejemplo:
* Manejar casos específicos:
```java
@ExceptionHandler(LocationNotFoundException.class)
public ResponseEntity<?> handleLocationNotFound(LocationNotFoundException ex) {
  //...
}
```

* Opción extra para cualquier problema de API externo no manejado específicamente:
```java
@ExceptionHandler(ExternalApiException.class)
public ResponseEntity<?> handleExternalApiGeneral(ExternalApiException ex) {
  // ...
}
```

Este diseño permite:
* **Manejo específico:** cuando es necesario (Ej: `LocationNotFoundException`).
* **Manejo genérico:** cuando es apropiado (`ExternalApiException`).
* **Extensibilidad futura:** agregar nuevas excepciones específicas sin cambiar los handlers existentes.

---

### 📝 Aclaración sobre Validaciones
El **frontend** hace todas las **validaciones del formulario**.
Decisión: no duplicarlas en el backend para evitar inconsistencias.

Separación de responsabilidades:
* **Frontend:** experiencia de usuario y validación inmediata.
* **Backend:** lógica de negocio e integración de APIs.

Además, el backend solo recibe solicitudes de **mi propio frontend**, por lo que no necesita protegerse de clientes "maliciosos".

---

## 🗺️ Mapa con Trazado de Ruta 
En cuanto al mapa que grafica la **ruta entre el origen y el destino en la respuesta**, el **backend proporciona las coordenadas y el camino**, mientras que el **frontend proporciona su visualización** usando **Leaflet**. 

---

### 📄 Salida de Registro (logging)
Los logs muestran siempre:

**1. Direcciones ingresadas.**
**2. Coordenadas y confianza.**
**3. Tipo de coincidencia.**
**4. Distancia calculada.**
**5. Decisión final.**

- ✔️ **Éxito** → geocodificación correcta, ruta calculada.
- ⚠️ **Warn** → problemas menores (ej. calle no hallada).
- ❌ **Fallo** → dirección fuera de rango, se rechaza el resultado.

Cada clase instancia su propio **logger:**
```java
private static final Logger logger = LoggerFactory.getLogger(MiClase.class)
```
Esto **evita** usar un **Singleton** y permite identificar con precisión el **origen de cada mensaje** en los logs. Además, los loggers son **thread-safe por defecto**, por lo que esta estrategia es más simple y segura. La repetición de la línea en cada clase no representa un problema frente a los beneficios que aporta.

---

## 📌 Anotaciones Frontend

### 🧩 Componentes Principales
- `Header.jsx` → logo.
- `ShippingForm.jsx` → formulario.
- `AddressSection.jsx` → bloque de dirección (origen y destino).
- `PackageSection.jsx` → peso y dimensiones.
- `ShippingResult.jsx` → resultados.
- `RouteMap.jsx` → mapa interactivo.
- `ServiceTypeSection.jsx` *(agregado en Feature 1)* → selección del tipo de servicio (Normal / Express / Prioritario).

---

### ✅ Validaciones en el Formulario
El formulario aplica validaciones de manera **preventiva**:

- Si los datos no cumplen las restricciones, el botón de cotizar se deshabilita automáticamente.
  
- No se muestran mensajes de error después del envío, ya que la idea es evitar solicitudes inválidas desde el principio.

---

### 📱 Diseño Responsivo en Formularios
Los selects de provincias, departamentos y municipios están diseñados para adaptarse a distintos tamaños de pantalla y a textos largos (nombres extensos como *Tierra del Fuego, Antártida e Islas del Atlántico Sur* —ejemplo real de una provincia argentina).

- **Textos largos:** uso de *text-wrap* y *word-wrap* para que siempre se vean completos.

- **Truncamiento:** en pantallas pequeñas, se aplica *truncate-text* con puntos suspensivos `(...)` cuando no hay suficiente espacio.

- **Dropdowns adaptables:**
	* Altura máxima con scroll en la lista *(max-height)*.
	* Ajustes dinámicos en *padding* y *font-size* para móviles y pantallas muy pequeñas mediante media queries.

- **Accesibilidad visual:** elementos deshabilitados muestran opacidad reducida *(--opacity-disabled)*.

En resumen, el **CSS asegura que la interfaz sea legible, usable y estética en cualquier dispositivo**, evitando cortes abruptos o superposiciones en los nombres de localidades.

---

### 💾 Caché de Datos
- **Provincias:** como son datos estáticos, se almacenan en **caché en memoria** durante toda la sesión. Esto permite **cargarlas solo una vez** y evita consultas repetidas al backend, **optimizando el rendimiento**.

- **Departamentos y Municipios:** se recargan dinámicamente según la **provincia seleccionada**, ya que dependen de la elección del usuario y pueden variar.

---

### 🔄 Flujo de interacción con la App
1. El usuario llega a la pantalla del formulario. El hook `useLocations` carga automáticamente la lista de **provincias argentinas**.

2. El usuario selecciona una **Provincia**. El hook `useLocations` lo detecta y automáticamente busca los **Departamentos correspondientes**. Luego, al seleccionar un Departamento, busca los **Municipios**.

3. El usuario hace clic en "**¡Cotizar Envío!**". Si el formulario está completo y correcto, el hook `useShippingCalculator` envía todos los datos al servidor.

4. El formulario muestra una **animación de carga** para que el usuario sepa que la aplicación está trabajando.

5. Resultados:
- ✔️ **Éxito:** El servidor responde con la cotización y otros detalles como las instrucciones de ruta y el mapa. 
- ❌ **Error:** Si hay un problema (por ejemplo, no se puede encontrar una de las direcciones), `shippingService` junto con `apiErrorHandler` trabajan para mostrar un claro mensaje de error en rojo en la parte superior del formulario, explicando lo que salió mal, y el usuario se queda en el formulario para corregirlo.

El objetivo principal de esta estructura fue mantener las cosas **ordenadas y separadas**. Cada parte del código tiene **una sola función**, lo que hace que toda la aplicación sea más **fácil de entender, arreglar y mejorar en el futuro**.

---

## ➡️ Flujo Completo

### ⚙️ Backend:
1. **Recibe el origen y el destino del usuario.**
2. Se realiza la geocodificación con **GeoApify** para **convertir las direcciones en coordenadas** lo más precisas posibles.
3. Se realiza el **cálculo de la ruta** con **GeoApify**. Calcula la ruta entre las coordenadas y devuelve:
   * Un **array de puntos** de coordenadas que forman el camino (conocido como la Geometría de la ruta).
   * Las **coordenadas** del origen y del destino.
   * La **distancia, el tiempo y las instrucciones** de ruta.
4. El backend crea un objeto `MapData` con:
   * **coordenadasRuta:** Array de pares [longitud, latitud] que forman el camino de la ruta.
   * **coordenadasOrigen:** [longitud, latitud] del punto de partida.
   * **coordenadasDestino:** [longitud, latitud] del punto de llegada.

---

### 🎨 Frontend (react-leaflet):
1. React recibe los datos: el componente `RouteMap.jsx` **obtiene el objeto** `MapData`.
2. Se convierten las coordenadas del formato **[lng, lat]** de **GeoApify** al formato **[lat, lng]** que usa **Leaflet**.
3. La librería **react-leaflet crea un mapa interactivo** usando los mosaicos (Teselas) de **OpenStreetMap**.
4. Agrega:
* **Marcador rojo** → origen.
* **Marcador celeste** → destino.
* **Polilínea azul** → ruta.
5. El usuario puede **mover y hacer zoom** en el mapa.

---

## 💡 Aprendizajes y Desafíos

- **Integración de APIs externas:** aprendí a combinar datos de **GeoRef** y **GeoApify** para resolver un problema concreto.
Esto me permitió **manejar diferentes fuentes, validar resultados y unificarlos en una sola respuesta coherente**.

- **Visualización de datos en mapas:** experimenté con **Leaflet** para renderizar mapas interactivos y mostrar información de rutas.
Fue un paso importante para **conectar la lógica del backend con una representación visual clara** para el usuario.

- **Uso de React-Leaflet:** trabajar con esta librería me permitió integrar mapas en un proyecto de **React** de forma más declarativa y sencilla. **Aprendí a transformar los datos devueltos por la API** (formato [lng, lat]) al formato esperado por **Leaflet** ([lat, lng]) y a **representar marcadores y rutas** en el mapa.

- **Manejo de errores y validaciones:** trabajar con direcciones que podían no existir o estar fuera del rango de 2 km fue un gran desafío. Comprendí la importancia de **diseñar reglas claras de validación** y de ofrecer **mensajes de error útiles y amigables**, manteniendo el backend robusto.

**En resumen**, este proyecto me ayudó a **ganar confianza en la integración de servicios externos** y en la **visualización de datos geoespaciales** dentro de una aplicación web.

---

## 🆕 Feature 1 – Tipos de Servicio 

📅 **Fecha**: *2025-10-07*  

Se agregó la posibilidad de seleccionar un tipo de servicio de envío (**Normal**, **Express** o **Prioritario**).
Esta mejora hace que la aplicación simule con mayor realismo una cotizadora de envíos profesional, permitiendo variar el precio y tiempo estimado según la modalidad elegida por el usuario.


🎯 **Objetivo**: Ajustar el costo y el tiempo estimado según la modalidad de envío elegida por el usuario.


🧩 **Impacto**: Afecta solo al cálculo del costo y tiempo estimado.


⚙️ **Implementación**
- Frontend
    * Nuevo componente `ServiceTypeSection.jsx` integrado en `ShippingForm.jsx`.
    
- Backend:
    * Nuevo `enum ServiceType`.
    * Campo `serviceType` agregado en `ShipmentRequest`.
    * Ajustes en `ShipmentService` y `ShipmentCalculator` para incorporar el tipo de servicio en los cálculos.
    
---

📌 **Nota**: Para una descripción general del proyecto, instrucciones de uso y guía para usuarios finales, consulta la [documentación oficial](./README.md).
