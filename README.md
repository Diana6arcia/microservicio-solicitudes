# Microservicio de Registro y Consulta de Solicitudes

Microservicio desarrollado como parte del desafío técnico de **Arquitectura Aplicativa**.

El proyecto implementa un API REST para el **registro y consulta de solicitudes**, con dos implementaciones:

* **MuleSoft**
* **Java 21 + Spring Boot**

Ambas implementaciones utilizan una base de datos **MySQL** y un servidor **SFTP** local para almacenar una copia de la evidencia asociada a cada solicitud.

---

## 1. Descripción

El microservicio permite:

* Registrar una nueva solicitud.
* Consultar una solicitud existente mediante su identificador.
* Validar los datos recibidos.
* Persistir la información en MySQL.
* Generar un archivo JSON como evidencia del registro.
* Copiar la evidencia al servidor SFTP.
* Manejar errores de validación, consulta y procesamiento.
* Generar un `correlationId` para facilitar el seguimiento de las operaciones.

### Endpoints

| Método | Endpoint                   | Descripción                   |
| ------ | -------------------------- | ----------------------------- |
| POST   | `/api/v1/solicitudes`      | Registra una nueva solicitud  |
| GET    | `/api/v1/solicitudes/{id}` | Consulta una solicitud por ID |

---

# 2. Arquitectura

El repositorio contiene dos implementaciones del mismo servicio:

```text
                    ┌──────────────────────┐
                    │       Cliente        │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   API REST           │
                    │ /api/v1/solicitudes  │
                    └──────────┬───────────┘
                               │
                 ┌─────────────┴─────────────┐
                 │                           │
                 ▼                           ▼
        ┌─────────────────┐        ┌─────────────────┐
        │    MuleSoft     │        │ Java 21 /       │
        │                 │        │ Spring Boot     │
        └────────┬────────┘        └────────┬────────┘
                 │                           │
                 └─────────────┬─────────────┘
                               │
                    ┌──────────┴──────────┐
                    │                     │
                    ▼                     ▼
             ┌────────────┐        ┌────────────┐
             │   MySQL    │        │    SFTP    │
             │ solicitudes│        │   upload   │
             └────────────┘        └────────────┘
```

La base de datos almacena la información de la solicitud y el servidor SFTP recibe una copia del archivo JSON generado como evidencia.

---

# 3. Estructura del repositorio

```text
microservicio-solicitudes/
│
├── database/
│   └── init.sql
│
├── evidence/
│   └── .gitkeep
│
├── java-solicitudes-api/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── mulesoft-solicitudes-api/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── exchange-docs/
│   ├── mule-artifact.json
│   └── pom.xml
│
├── sftp/
│   └── upload/
│       └── .gitkeep
│
├── docker-compose.yml
├── solicitudes-api.raml
└── .gitignore
```

---

# 4. Modelo de datos

La entidad principal es `Solicitud`.

| Campo           | Descripción                            |
| --------------- | -------------------------------------- |
| `id`            | Identificador generado automáticamente |
| `clienteId`     | Identificador del cliente              |
| `tipo`          | Tipo de solicitud                      |
| `descripcion`   | Descripción de la solicitud            |
| `estado`        | Estado generado por la aplicación      |
| `fechaRegistro` | Fecha y hora del registro              |
| `correlationId` | Identificador de seguimiento           |

### Tipos permitidos

```text
ALTA
CAMBIO
BAJA
```

Los campos `estado`, `fechaRegistro` y `correlationId` son administrados por la aplicación y no son responsabilidad del consumidor del API.

---

# 5. Requisitos

Para ejecutar el proyecto se requiere:

* Windows, Linux o macOS.
* Git.
* Docker Desktop.
* Java 21.
* Maven (opcional, ya que el proyecto Java incluye Maven Wrapper).
* Anypoint Studio para la implementación MuleSoft.
* MySQL 8.x, ejecutado mediante Docker.
* Servidor SFTP, ejecutado mediante Docker.
* Cliente REST para pruebas, por ejemplo Postman o similar.

---

# 6. Dependencias mediante Docker

El archivo:

```text
docker-compose.yml
```

contiene los servicios necesarios para ejecutar las dependencias locales:

* MySQL
* SFTP

## 6.1 Levantar dependencias

Desde la raíz del proyecto:

```bash
docker compose up -d
```

Verificar los contenedores:

```bash
docker compose ps
```

Los servicios deberán encontrarse en estado `running`.

## 6.2 Detener dependencias

```bash
docker compose down
```

Para detener los contenedores y eliminar los recursos asociados:

```bash
docker compose down
```

---

# 7. MySQL

La base de datos utilizada por el proyecto es:

```text
Base de datos: solicitudes_db
```

La estructura inicial se encuentra en:

```text
database/init.sql
```

El script crea la tabla necesaria para almacenar las solicitudes.

La configuración de conexión utilizada por la aplicación debe corresponder a los valores definidos en `docker-compose.yml`.

---

# 8. SFTP

El proyecto utiliza un servidor SFTP local para almacenar una copia de las evidencias generadas.

La carpeta local utilizada para el intercambio es:

```text
sftp/upload/
```

Los archivos generados durante las pruebas no forman parte del repositorio.

El repositorio conserva únicamente:

```text
sftp/upload/.gitkeep
```

para mantener la estructura de directorios.

---

# 9. Implementación Java

La implementación Java se encuentra en:

```text
java-solicitudes-api/
```

Está desarrollada utilizando:

* Java 21
* Spring Boot
* Maven
* Spring Web
* Spring Data JPA
* MySQL
* SFTP

## 9.1 Configuración

La configuración se encuentra en:

```text
java-solicitudes-api/src/main/resources/application.properties
```

Antes de ejecutar la aplicación, verificar principalmente:

```properties
evidence.path=C:/proyectos/evidence
```

Esta ruta corresponde al repositorio donde se estarán almacenando las evidencias en formato json de las solicitudes creadas.

**Debe modificarse por una ruta válida en el equipo donde se despliegue la aplicación.**

También deben verificarse los parámetros de conexión a MySQL y SFTP de acuerdo con el entorno local.

---

# 10. Ejecutar la aplicación Java

Ingresar al directorio:

```bash
cd java-solicitudes-api
```

En Windows se puede utilizar el Maven Wrapper incluido:

```powershell
.\mvnw.cmd spring-boot:run
```

También puede utilizarse Maven instalado localmente:

```bash
mvn spring-boot:run
```

Una vez iniciada la aplicación, el API estará disponible en el puerto configurado en `application.properties`.

---

# 11. Implementación MuleSoft

La implementación MuleSoft se encuentra en:

```text
mulesoft-solicitudes-api/
```

El proyecto puede abrirse directamente desde **Anypoint Studio**.

## 11.1 Configuración

Los parámetros de conexión se encuentran en:

```text
mulesoft-solicitudes-api/src/main/resources/application.properties
```

Verificar las rutas y parámetros de:

* HTTP Listener
* MySQL
* Evidencias
* SFTP

antes de ejecutar el proyecto.

## 11.2 Ejecución

1. Abrir Anypoint Studio.
2. Importar el proyecto `mulesoft-solicitudes-api`.
3. Verificar las propiedades de configuración.
4. Ejecutar el proyecto como aplicación Mule.
5. Esperar a que el HTTP Listener quede iniciado.

---

# 12. RAML

La especificación de la API se encuentra en:

```text
solicitudes-api.raml
```

También se incluye una copia dentro del proyecto MuleSoft:

```text
mulesoft-solicitudes-api/src/main/resources/api/solicitudes-api.raml
```

La especificación documenta los endpoints, tipos de datos y respuestas esperadas.

---

# 13. Endpoint POST

## Registrar una solicitud

```http
POST /api/v1/solicitudes
```

### Request

```json
{
  "clienteId": "CLI001",
  "tipo": "ALTA",
  "descripcion": "Solicitud de alta de servicio"
}
```

### Respuesta exitosa

HTTP:

```text
201 Created
```

Ejemplo:

```json
{
  "id": 1,
  "clienteId": "CLI001",
  "tipo": "ALTA",
  "descripcion": "Solicitud de alta de servicio",
  "estado": "REGISTRADA",
  "fechaRegistro": "2026-09-15T20:00:00",
  "correlationId": "..."
}
```

El `id`, `estado`, `fechaRegistro` y `correlationId` son generados por la aplicación.

---

# 14. Validaciones del POST

La solicitud debe contener:

* `clienteId`
* `tipo`
* `descripcion`

El campo `tipo` solamente acepta:

```text
ALTA
CAMBIO
BAJA
```

Cuando la información no cumple las validaciones establecidas, el servicio devuelve:

```text
400 Bad Request
```

---

# 15. Flujo de registro

Al recibir una solicitud válida se realiza el siguiente proceso:

```text
1. Recibir request
       ↓
2. Generar correlationId
       ↓
3. Validar información
       ↓
4. Insertar solicitud en MySQL
       ↓
5. Obtener ID generado
       ↓
6. Generar archivo JSON de evidencia
       ↓
7. Guardar evidencia localmente
       ↓
8. Copiar evidencia al SFTP
       ↓
9. Responder 201 Created
```

---

# 16. Evidencia

Por cada solicitud registrada se genera un archivo JSON utilizando el identificador de la solicitud.

Ejemplo:

```text
solicitud-1.json
```

El archivo contiene información relacionada con la solicitud registrada.

La carpeta:

```text
C:/proyectos/evidence (único repositorio para los diferentes aplicativos: Mulesoft y Java)
```

se utiliza para almacenar las evidencias locales durante la ejecución.

Los archivos generados durante las pruebas están excluidos del repositorio mediante `.gitignore`.

---

# 17. Endpoint GET

## Consultar una solicitud

```http
GET /api/v1/solicitudes/{id}
```

Ejemplo:

```http
GET /api/v1/solicitudes/1
```

### Respuesta exitosa

HTTP:

```text
200 OK
```

La respuesta contiene la información almacenada de la solicitud y, cuando está disponible, la evidencia asociada.

---

# 18. Validación del ID

El parámetro `{id}` debe ser:

* Numérico.
* Entero.
* Mayor que cero.

Ejemplo válido:

```text
/api/v1/solicitudes/1
```

Ejemplos inválidos:

```text
/api/v1/solicitudes/0
/api/v1/solicitudes/-1
/api/v1/solicitudes/abc
```

Para un ID inválido se devuelve:

```text
400 Bad Request
```

---

# 19. Solicitud no encontrada

Cuando el ID recibido no existe en la base de datos, se devuelve:

```text
404 Not Found
```

Ejemplo:

```json
{
  "error": "Not Found",
  "detail": "Solicitud no encontrada"
}
```

---

# 20. Manejo de errores

Se contemplan diferentes escenarios de error:

| Escenario                        | HTTP |
| -------------------------------- | ---: |
| Datos de entrada inválidos       |  400 |
| ID inválido                      |  400 |
| Solicitud inexistente            |  404 |
| Error de base de datos           |  500 |
| Error al generar evidencia       |  500 |
| Error al enviar evidencia a SFTP |  500 |
| Error inesperado                 |  500 |

Las respuestas de error mantienen una estructura consistente mediante los objetos definidos para el API.

---

# 21. Correlation ID

Cada operación genera un identificador de correlación:

```text
correlationId
```

Este identificador permite relacionar los eventos asociados a una misma ejecución y facilita el seguimiento de la operación en los logs.

---

# 22. Pruebas

El API puede probarse utilizando Postman, Insomnia, curl o cualquier cliente HTTP.

### POST

```http
POST http://localhost:8080/api/v1/solicitudes
Content-Type: application/json
```

Body:

```json
{
  "clienteId": "CLI001",
  "tipo": "ALTA",
  "descripcion": "Solicitud de prueba"
}
```

### GET

```http
GET http://localhost:8080/api/v1/solicitudes/1
```

El puerto puede variar dependiendo de la configuración utilizada para cada implementación.

---

# 23. Pruebas recomendadas

Para validar el funcionamiento completo se recomienda ejecutar al menos los siguientes escenarios:

### Registro exitoso

```text
POST con información válida
→ 201 Created
→ Registro en MySQL
→ Archivo de evidencia
→ Copia en SFTP
```

### Datos inválidos

```text
POST sin campos obligatorios
→ 400 Bad Request
```

### Tipo inválido

```text
POST con tipo diferente de ALTA/CAMBIO/BAJA
→ 400 Bad Request
```

### Consulta exitosa

```text
GET con ID existente
→ 200 OK
```

### Solicitud inexistente

```text
GET con ID no registrado
→ 404 Not Found
```

### ID inválido

```text
GET /api/v1/solicitudes/abc
→ 400 Bad Request
```

---

# 24. Git

El proyecto se encuentra versionado mediante Git.

Para obtener el código:

```bash
git clone https://github.com/Diana6arcia/microservicio-solicitudes.git
```

Ingresar al proyecto:

```bash
cd microservicio-solicitudes
```

---

# 25. Consideraciones

* Las rutas locales deben ajustarse al entorno donde se ejecute el proyecto.
* La ruta configurada para las evidencias no debe apuntar necesariamente a `C:/proyectos/evidence`; debe utilizarse una ruta válida en el equipo de ejecución.
* Los archivos generados durante las pruebas no se incluyen en el repositorio.
* MySQL y SFTP se ejecutan localmente mediante Docker.
* Las configuraciones de conexión deben verificarse antes de iniciar cada implementación.
* MuleSoft y Java implementan la misma funcionalidad funcional solicitada en el desafío.

---

# 26. Tecnologías utilizadas

```text
Java 21
Spring Boot
Maven
MuleSoft
Anypoint Studio
RAML
REST
MySQL 8
SFTP
Docker
Docker Compose
Git
GitHub
```

---

# 27. Autor

**Diana García**

Proyecto desarrollado como parte de un desafío técnico de Arquitectura Aplicativa.
