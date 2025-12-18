# Descripción del proyecto

Este proyecto consiste en el desarrollo de una aplicación orientada a la gestión de un sistema de monopatines eléctricos, diseñada bajo una arquitectura de microservicios. El objetivo principal es ofrecer una solución escalable y modular que permita administrar de forma eficiente los distintos componentes del sistema, facilitando su mantenimiento y evolución.

La aplicación fue desarrollada utilizando Spring Boot para la creación de los microservicios, los cuales se comunican entre sí de manera desacoplada. Cada microservicio es responsable de una parte específica de la lógica del negocio, lo que permite una mejor organización del código y una mayor independencia entre componentes.

Para la persistencia de datos, se implementó JPA/Hibernate, estableciendo conexión con distintas bases de datos según la responsabilidad de cada microservicio. Esto permite una gestión adecuada de la información, garantizando la integridad de los datos y facilitando las operaciones de alta, baja, modificación y consulta.

El proyecto fue desarrollado de forma colaborativa, aplicando buenas prácticas de programación y principios de diseño orientados a la construcción de aplicaciones robustas, escalables y alineadas con estándares utilizados en entornos profesionales de desarrollo de software.

# Documentación de Endpoints – Trabajo Integrador 4 (Primera Parte)

## 🔗 Swagger UI – Documentación por Microservicio

Podés acceder a la documentación interactiva de cada microservicio a través de los siguientes enlaces:

- **Usuarios Service** → [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)  
- **Viajes Service** → [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)  
- **Monopatines Service** → [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)  
- **Paradas Service** → [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html)  
- **Tarifas Service** → [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html)

---


📘 **Notas generales**  
- Todos los endpoints están accesibles a través del API Gateway (`localhost:8082`).  
- Los campos de tipo fecha deben seguir el formato `yyyy-mm-dd`.
- La base de datos utilizada es Postgres, crearlas con los siguientes nombres antes de levantar el proyecto: micro_viaje, base_paradas, microservicio_monopatin_db, base-microservicio, usuarios_db.


## a. Reporte de uso de monopatines
**Objetivo:** Permite generar un reporte de uso por kilómetros, configurable para incluir o no los tiempos de pausa.  
**Endpoint:**  
GET http://localhost:8082/api/monopatines/reporte-uso?incluirPausas=true  
**Parámetros:**  
- incluirPausas *(booleano, opcional)*: true o false. Determina si el reporte considera los tiempos de pausa.  
**Microservicio:** monopatines-service

---

## b. Anular cuenta de usuario (desde el csv se cargan 5 usuarios y 5 cuentas, es decir ids disponibles para pruebas: 1,2,3,4,5)
**Objetivo:** Permite al administrador anular (inhabilitar temporalmente) una cuenta de usuario.  
**Endpoint:**  
PUT http://localhost:8082/api/cuentas/{id}/anular  
**Parámetros de ruta:**  
- {id}: ID del usuario a anular.  
**Microservicio:** cuentas-service

---

## c. Monopatines con más de X viajes en un año
**Objetivo:** Consulta los monopatines que superan una cantidad de viajes durante un año específico.  
**Endpoint:**  
GET http://localhost:8082/viajes/reporte/monopatines?anio=&minViajes=  
**Parámetros:**  
- anio *(int, obligatorio)*: Año a consultar.  
- minViajes *(int, obligatorio)*: Número mínimo de viajes.  
**Microservicio:** viajes-service

---

## d. Total facturado en un rango de meses
**Objetivo:** Consulta el total facturado dentro de un rango de meses en un año determinado.  
**Endpoint:**  
GET http://localhost:8082/viajes/facturacion?anio=&mesInicio=&mesFin=  
**Parámetros:**  
- anio *(int, obligatorio)*  
- mesInicio *(int, obligatorio)*  
- mesFin *(int, obligatorio)*  
**Microservicio:** viajes-service

---

## e. Usuarios que más usan los monopatines
**Objetivo:** Obtiene los usuarios con mayor uso de monopatines, filtrados por período y tipo de usuario.  
**Endpoint:**  
GET http://localhost:8082/api/usuarios/top-usuarios?fechaInicio=&fechaFin=&tipoUsuario=  
**Parámetros:**  
- fechaInicio *(string, formato yyyy-mm-dd, obligatorio)*  
- fechaFin *(string, formato yyyy-mm-dd, obligatorio)*  
- tipoUsuario *(string, opcional)*: Por ejemplo "premium" o "standard".  
**Microservicio:** usuarios-service

---

## f. Ajuste de precios
**Objetivo:** Permite registrar un nuevo ajuste de precios, que entrará en vigencia a partir de una fecha determinada.  
**Endpoint:**  
POST http://localhost:8082/tarifa/nueva
**Microservicio:** tarifas-service

---

## g. Monopatines cercanos a mi ubicación
**Objetivo:** Permite a un usuario buscar monopatines cercanos a su zona.  
**Endpoint:**  
GET http://localhost:8082/paradas/cercanas?lat=&long=  
**Parámetros:**  
- lat *(float, obligatorio)*: Latitud del usuario.  
- long *(float, obligatorio)*: Longitud del usuario.  
**Microservicio:** paradas-service

---

## h. Uso personal de monopatines
**Objetivo:** Permite al usuario consultar cuánto ha usado los monopatines en un período, con la opción de incluir uso de usuarios relacionados.  
**Endpoint:**  
GET http://localhost:8082/api/usuarios/uso?fechaInicio=&fechaFin
**Parámetros:**  
- fechaInicio *(string, formato yyyy-mm-dd, obligatorio)*  
- fechaFin *(string, formato yyyy-mm-dd, obligatorio)*   
**Microservicio:** usuarios-service

---
