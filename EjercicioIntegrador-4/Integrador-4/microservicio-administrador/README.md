Este microservicio forma parte del ecosistema de alquiler de monopatines
y se encarga de administrar las tarifas del sistema, es decir, todos los
valores utilizados para calcular el costo de un viaje, pausas, recargos y
promociones.

Ofrece una API clara, organizada y documentada mediante Swagger / OpenAPI. 
La lógica se encuentra desacoplada en capas de Controller–Service–Repository.

## Características principales
CRUD completo de Tarifas.
Manejo de tipos de tarifa mediante un enum (NORMAL, PROMOCIONAL, etc.).
Selección automática de la tarifa vigente según fecha y tipo.
Histórico completo de tarifas anteriores.
Lógica de negocio para detectar promociones activas y priorizarlas.
Documentación automática con Swagger.
## 📦 Modelos del Sistema
💲 Tarifa
Representa una estructura de costos utilizada para calcular el precio del
servicio.
Campos principales:
id
tipo (enum: NORMAL, PROMOCIONAL, etc.)
costoPorMinuto
costoReanudacionPausa
vigenteDesde
vigenteHasta (nullable: si es NULL, está vigente hasta nuevo cambio)

## Otros valores relacionados al cálculo de costos
Lógica clave:
Solo una tarifa por tipo puede estar vigente al mismo tiempo.
Las tarifas anteriores quedan guardadas como historial.
Para tarifas PROMOCIONALES se consideran los intervalos de fecha.
Si hay una tarifa promocional vigente, esta se aplica antes que la normal.

## 🔍 Obtención de Tarifa Vigente
El repositorio incluye consultas especializadas:
findByTipo(tipoTarifa tipo)
Devuelve la última tarifa registrada de un tipo específico, ordenada por
fecha descendente.
findTarifaPromocionalVigente()
Consulta nativa que retorna:
La tarifa promocional vigente hoy (si existe).
En caso contrario, la tarifa NORMAL cuya vigencia no haya expirado.
Esto permite que el sistema aplique automáticamente la tarifa correcta sin intervención manual.