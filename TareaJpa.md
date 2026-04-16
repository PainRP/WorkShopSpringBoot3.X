# Tarea: integrar JPA con PostgreSQL y CRUD completo

Este documento es la **guía para el siguiente paso** del proyecto [springboot-api-demo](README.md): agregar **Spring Data JPA**, conectar **PostgreSQL**, definir **dos entidades** cuyo comportamiento de esquema se **configure desde `application.properties`**, y exponer **APIs REST con CRUD completo** siguiendo buenas prácticas.

**Requisitos previos:** Java 17, Maven 3.9+, PostgreSQL instalado y una base de datos creada (puede ser local o contenedor).

---

## Objetivos de aprendizaje

- Configurar datasource y JPA/Hibernate leyendo valores desde propiedades.
- Entender la **sincronización del esquema** (tablas/columnas) controlada por propiedades como `spring.jpa.hibernate.ddl-auto`.
- Modelar al menos **dos entidades JPA** con relación de negocio coherente.
- Implementar **capas** separadas: entidad → repositorio → servicio → controlador → DTOs.
- CRUD completo (crear, leer uno, leer lista, actualizar, eliminar) con **códigos HTTP** y **validación** adecuados.
- Reutilizar el manejo de errores existente (`GlobalExceptionHandler`) y ampliarlo si hace falta (por ejemplo, recurso no encontrado).

---

## Parte 1 — Dependencias Maven (sugeridas)

En `pom.xml`, dentro de `<dependencies>`, agrega:

1. **Spring Data JPA** (incluye Hibernate como proveedor JPA).
2. **Driver de PostgreSQL**.

Ejemplo:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

No elimines las dependencias que ya tiene el proyecto (`web`, `validation`, `springdoc`, tests).

Ejecuta:

```bash
mvn -q dependency:resolve
```

---

## Parte 2 — Base de datos PostgreSQL

1. Crea una base de datos, por ejemplo: `workshop_jpa`.
2. Anota usuario, contraseña, host y puerto (por defecto `5432`).

Para desarrollo local puedes usar variables de entorno y **no** commitear secretos. Spring Boot permite referenciar env vars en `application.properties` con la sintaxis `${NOMBRE:valorPorDefecto}`. Agregar el application.properties en el .gitignore

---

## Parte 3 — Propiedades: conexión y sincronización del esquema

Edita `src/main/resources/application.properties` (o crea `application-local.properties` y activa el perfil `local`).

### 3.1 Datasource

Agrega propiedades estándar, por ejemplo:

```properties
# PostgreSQL (ajusta usuario/clave/host según tu entorno)
spring.datasource.url=jdbc:postgresql://localhost:5432/workshop_jpa
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 3.2 JPA / Hibernate — “sincronizar desde el archivo de propiedades”

La **sincronización del modelo JPA con las tablas** se controla principalmente con:

| Propiedad | Uso típico en desarrollo | Uso típado en producción |
|-----------|---------------------------|---------------------------|
| `spring.jpa.hibernate.ddl-auto` | `update` o `create-drop` para pruebas | `validate` o migraciones externas (Flyway/Liquibase) |

Valores comunes de `ddl-auto`:

- **`none`**: Hibernate no toca el esquema.
- **`validate`**: Compara entidades con el esquema; falla si no coinciden (útil con scripts de migración).
- **`update`**: Aplica cambios incrementales al esquema (cómodo en desarrollo; revisar limitaciones).
- **`create`**: Borra y recrea el esquema al arrancar.
- **`create-drop`**: Como `create`, y elimina al apagar (solo para pruebas).

Ejemplo:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Requisito de la tarea:** las entidades deben “sincronizarse” con la base **según lo definido en propiedades**: documenta en tu entrega qué valor usaste para `ddl-auto` y por qué. En producción real se prefiere `validate` + migraciones; indica esa alternativa en un párrafo breve en tu README o informe.

### 3.3 Perfiles (opcional pero recomendado)

- `application.properties`: valores por defecto o comunes.
- `application-dev.properties`: `ddl-auto=update`, `show-sql=true`.
- `application-prod.properties`: `ddl-auto=validate`, sin volcar SQL en logs.

Arranque con perfil:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Investigar como arrancar el perfil desde Eclipse pasando como parametro el perfil
---

## Parte 4 — Dos entidades JPA (ejemplo sugerido)

Debes definir **dos entidades** con una relación clara. Ejemplo didáctico alineado a un taller:

| Entidad principal | Entidad relacionada | Relación |
|-------------------|---------------------|----------|
| `Categoria` | `Producto` | Una categoría tiene muchos productos (`@OneToMany` / `@ManyToOne`) |

**Lineamientos:**

- Usar `Long` o `UUID` como tipo de identificador; `@GeneratedValue` apropiado.
- Campos obligatorios con restricciones (`nullable = false`, `length`, etc.).
- **Auditoría opcional:** `Instant` o `LocalDateTime` para `creadoEn` / `actualizadoEn` con `@PrePersist` / `@PreUpdate`.
- Evitar exponer la entidad directamente en la API; usar **DTOs** de request/response (como ya hace el proyecto con `SaludoRequest` / `SaludoResponse`).
- Nombres de tabla explícitos con `@Table(name = "...")` para evitar sorpresas con convenciones.

**Ubicación sugerida de paquetes** (consistente con el proyecto):

- `com.ejemplo.demo.domain.model` — entidades JPA.
- `com.ejemplo.demo.domain.repository` — interfaces `JpaRepository`.
- `com.ejemplo.demo.domain.service` — lógica de negocio y transacciones.
- `com.ejemplo.demo.api.controller` — controladores REST.
- `com.ejemplo.demo.api.dto` — DTOs de entrada/salida.

---

## Parte 5 — Repositorios

Para cada entidad, crea una interfaz que extienda `JpaRepository<Entity, Id>`.

Buenas prácticas:

- Métodos de consulta derivados del nombre (`findBy...`) cuando sean simples.
- Para consultas complejas, `@Query` con JPQL o native query documentada.
- Evitar lógica de negocio en el repositorio; solo acceso a datos.

---

## Parte 6 — Servicio de dominio

Implementa servicios `@Service` que:

- Orquesten validaciones de negocio (por ejemplo: no permitir SKU duplicado, categoría inexistente).
- Usen `@Transactional` en métodos que modifiquen datos (`readOnly = true` en lecturas cuando aplique).
- Lanzen excepciones claras:
  - `IllegalArgumentException` para reglas de negocio (ya mapeadas a `400` en `GlobalExceptionHandler`).
  - Para “no encontrado”, usa `jakarta.persistence.EntityNotFoundException` o una excepción propia y **agrega** un `@ExceptionHandler` que responda `404` con un cuerpo similar a `ErrorResponse`.

---

## Parte 7 — APIs REST: CRUD completo y buenas prácticas

Implementa **dos recursos REST** (uno por entidad principal del dominio), por ejemplo:

- `/api/v1/categorias`
- `/api/v1/productos`

### 7.1 Operaciones mínimas por recurso

| Operación | Método HTTP | Ruta típica | Respuesta exitosa |
|-----------|-------------|-------------|-------------------|
| Listar | `GET` | `/api/v1/categorias` | `200` + lista (JSON) |
| Obtener por id | `GET` | `/api/v1/categorias/{id}` | `200` o `404` |
| Crear | `POST` | `/api/v1/categorias` | `201` + body + header `Location` |
| Actualizar | `PUT` o `PATCH` | `/api/v1/categorias/{id}` | `200` o `204` / `404` |
| Eliminar | `DELETE` | `/api/v1/categorias/{id}` | `204` o `404` |

**Buenas prácticas:**

1. **DTOs** con `jakarta.validation` (`@NotBlank`, `@Size`, `@Min`, `@DecimalMin`, etc.) en los bodies de entrada.
2. **No** devolver entidades JPA; mapear a DTOs (manualmente o con MapStruct si el equipo lo acuerda).
3. **Paginación** en listados: `Pageable` de Spring Data y parámetros `page`, `size`, `sort` (recomendado si la lista puede crecer).
4. **Documentar** cada endpoint en OpenAPI (anotaciones `@Operation`, `@ApiResponse` de `io.swagger.v3.oas.annotations` o equivalentes ya compatibles con springdoc).
5. **Consistencia** con el prefijo `/api/v1` usado en el proyecto.
6. **Puerto:** el proyecto usa `server.port=8081` en `application.properties`; las URLs de prueba deben usar ese puerto si no lo cambias.

### 7.2 Ejemplos de prueba con `curl` (ajusta ids y puerto)

```bash
# Crear categoría
curl -s -X POST http://localhost:8081/api/v1/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Bebidas","descripcion":"Gaseosas y jugos"}'

# Listar
curl -s http://localhost:8081/api/v1/categorias

# Obtener por id
curl -s http://localhost:8081/api/v1/categorias/1

# Actualizar
curl -s -X PUT http://localhost:8081/api/v1/categorias/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Bebidas frías","descripcion":"Actualizado"}'

# Eliminar
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE http://localhost:8081/api/v1/categorias/1
```

Repite el patrón para el segundo recurso (`productos`), incluyendo la **asociación** a categoría (por id en el DTO o subrecurso, según diseño que documentes).

---

## Parte 8 — Pruebas automatizadas

Mínimo esperado:

- **Tests de integración** con `@SpringBootTest` + `@AutoConfigureMockMvc` (o `@WebMvcTest` para capa web aislada) que verifiquen:
  - creación válida → `201`;
  - validación fallida → `400`;
  - recurso inexistente → `404` (si implementaste el handler).

Para tests que golpean la base real, valorar **`@DataJpaTest`** o Testcontainers con PostgreSQL; si usan H2 en memoria para tests, documenten la limitación (dialecto y diferencias con PostgreSQL).

Ejecutar:

```bash
mvn test
```

---

## Parte 9 — Entregables (checklist)

- [ ] `pom.xml` con `spring-boot-starter-data-jpa` y driver PostgreSQL.
- [ ] `application.properties` (y/o perfiles) con **datasource** y **`spring.jpa.hibernate.ddl-auto`** explicado.
- [ ] **Dos entidades JPA** con relación y tablas generadas/actualizadas según la configuración.
- [ ] Repositorios Spring Data.
- [ ] Servicios con transacciones y reglas de negocio básicas.
- [ ] **Dos APIs** con **CRUD completo**, DTOs y validación.
- [ ] Documentación OpenAPI/Swagger actualizada (`http://localhost:8081/swagger-ui/index.html`).
- [ ] Manejo de **404** (u otras excepciones JPA) integrado de forma ordenada.
- [ ] Pruebas que pasen en `mvn test`.

---

## Notas finales

- La frase **“sincronizar desde el archivo de propiedades”** en esta tarea se cumple configurando JPA/Hibernate (sobre todo `ddl-auto` y el datasource) **solo desde propiedades**, sin hardcodear credenciales ni URLs en el código Java.
- Si el curso exige datos iniciales, puedes usar `data.sql` / `import.sql` con `spring.sql.init.mode=always` **solo en dev**, o un `@Component` que inserte datos al arrancar; documenta el enfoque elegido.
- Mantén el estilo del proyecto: inyección por constructor, paquetes claros y respuestas de error coherentes con `ErrorResponse`.
