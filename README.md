# Workshop Spring Boot 3.x + JDK 17 (Maven)

Este repositorio es un **tutorial paso a paso** para clase/laboratorio.
Los estudiantes avanzan descomentando bloques, probando endpoints y completando ejercicios.

## Objetivo del taller

- Practicar arquitectura basica de API REST con Spring Boot.
- Aplicar validaciones con `jakarta.validation`.
- Manejar errores de forma global.
- Documentar la API con Swagger/OpenAPI.
- Implementar un endpoint de negocio mas desafiante.

---

## 0) Requisitos

- Java `17`
- Maven `3.9+`

Verifica Java:

```bash
java -version
```

Debe mostrar `17.x`.

Si usas macOS y necesitas cambiar Java temporalmente:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
```

---

## 1) Comando para generar el proyecto Spring Boot

Este comando usa Spring Initializr y genera un proyecto Maven con Java 17 y Spring Boot 3.x:

```bash
curl https://start.spring.io/starter.zip \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=3.3.5 \
  -d groupId=com.ejemplo \
  -d artifactId=springboot-api-demo \
  -d name=springboot-api-demo \
  -d packageName=com.ejemplo.demo \
  -d javaVersion=17 \
  -d dependencies=web,validation \
  -o springboot-api-demo.zip
```

```bash
unzip springboot-api-demo.zip -d .
cd springboot-api-demo
```

---

## 2) Paso a paso del taller

### Paso 1 - Ejecutar base del proyecto

```bash
mvn spring-boot:run
```

Probar endpoint base:

```bash
curl http://localhost:8080/api/v1
```

Respuesta esperada:

```json
{
  "estado": "ok",
  "mensaje": "Workshop Spring Boot activo"
}
```

### Paso 2 - Habilitar endpoint GET de saludos

En `src/main/java/com/ejemplo/demo/api/controller/SaludoController.java`:

- Descomentar bloque `PASO 2`.
- Descomentar imports indicados.
- Descomentar inyeccion de `SaludoService`.
- Descomentar endpoint `@GetMapping("/saludos")`.

Probar:

```bash
curl "http://localhost:8080/api/v1/saludos?nombre=Ana"
```

### Paso 3 - Habilitar endpoint POST con validacion

En `SaludoController`:

- Descomentar bloque `PASO 3`.
- Descomentar imports (`@PostMapping`, `@RequestBody`, `@Valid`, `SaludoRequest`).

Probar caso correcto:

```bash
curl -X POST http://localhost:8080/api/v1/saludos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana"}'
```

Probar caso invalido:

```bash
curl -X POST http://localhost:8080/api/v1/saludos \
  -H "Content-Type: application/json" \
  -d '{"nombre":""}'
```

Debe responder `400` en el caso invalido.

### Paso 4 - Ejercicio de logica de negocio

En `src/main/java/com/ejemplo/demo/domain/service/SaludoService.java`:

- Completar logica del metodo `normalizarNombre`.
- Recomendaciones:
  - Quitar espacios al inicio/final.
  - Convertir primera letra a mayuscula.
  - Validar reglas de negocio (opcional).

### Paso 5 - Manejo de errores de negocio

En `src/main/java/com/ejemplo/demo/api/exception/GlobalExceptionHandler.java`:

- Descomentar bloque `PASO 5`.
- Ajustar respuesta para `IllegalArgumentException`.

Objetivo: si hay error de negocio, responder `400` con codigo `BUSINESS_RULE_ERROR`.

### Paso 6 - Completar pruebas

En `src/test/java/com/ejemplo/demo/api/controller/SaludoControllerTest.java`:

- Completar bloque `PASO 6`.
- Agregar pruebas para:
  - GET `/api/v1/saludos`
  - POST invalido con validacion

Ejecutar:

```bash
mvn test
```

### Paso 7 - Documentar API con Swagger/OpenAPI

Objetivo: exponer y probar endpoints desde UI de documentacion.

1. Agregar dependencia en `pom.xml`:

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.6.0</version>
</dependency>
```

2. Levantar proyecto y abrir:

- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

3. Agregar metadata (recomendado):

- Titulo
- Version
- Descripcion
- Contacto

### Paso 8 - Crear endpoint desafiante (obligatorio)

Implementar un endpoint mas real que un saludo.

#### Opcion sugerida: Simulador de Prestamo

- Endpoint: `POST /api/v1/simulaciones/prestamo`
- Request:
  - `monto` (BigDecimal, > 0)
  - `tasaAnual` (BigDecimal, > 0)
  - `meses` (int, entre 1 y 360)
- Response:
  - `cuotaMensual`
  - `interesTotal`
  - `totalPagar`

Requisitos:

- Validar datos de entrada con `jakarta.validation`.
- Implementar logica en una clase de servicio.
- Manejar errores de negocio en `GlobalExceptionHandler`.
- Documentar endpoint en Swagger.
- Crear al menos 2 pruebas:
  - caso exitoso
  - caso invalido

Formula sugerida (cuota fija):

```text
cuota = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
```

Donde:

- `P` = monto
- `r` = tasa mensual (`tasaAnual / 12 / 100`)
- `n` = numero de meses

---

## Demo adicional: Singleton vs no singleton

Esta comparacion ya esta implementada en el proyecto para demostracion en clase.

### Caso singleton (con `@Service`)

```bash
curl -X POST http://localhost:8080/api/v1/demo/estado/singleton/reset
curl -X POST http://localhost:8080/api/v1/demo/estado/singleton/25
curl http://localhost:8080/api/v1/demo/estado/singleton
```

En la segunda llamada se mantiene el valor (`25`), porque Spring reutiliza la misma instancia.

### Caso manual (sin `@Service`, usando `new`)

```bash
curl -X POST http://localhost:8080/api/v1/demo/estado/manual/25
curl http://localhost:8080/api/v1/demo/estado/manual
```

La consulta devuelve `0`, porque cada endpoint crea una instancia nueva.

---

## Checklist final

- [ ] Proyecto corre en local
- [ ] GET `/api/v1` responde OK
- [ ] GET `/api/v1/saludos` habilitado
- [ ] POST `/api/v1/saludos` habilitado y validando
- [ ] Reglas de negocio implementadas
- [ ] Manejo de errores de negocio implementado
- [ ] Swagger/OpenAPI habilitado y accesible
- [ ] Endpoint nuevo implementado
- [ ] Tests del endpoint nuevo en verde
- [ ] Pruebas pasando (`mvn test`)

---

## Incremento progresivo (semana 13 al 17 de abril)

Como siguiente etapa del curso, este proyecto incorpora un incremento progresivo enfocado en persistencia con JPA.

- Periodo de trabajo: **del 13 al 17 de abril**.
- Guia oficial del incremento: [`TareaJpa.md`](TareaJpa.md).
- Alcance general:
  - Integrar Spring Data JPA.
  - Configurar base de datos (PostgreSQL en la tarea principal).
  - Modelar 2 entidades relacionadas.
  - Exponer 2 APIs con CRUD completo y buenas practicas.

Recomendacion para clase: pueden realizar una demo inicial con H2 para reducir friccion de entorno, y luego completar la entrega final siguiendo los lineamientos de `TareaJpa.md`.
