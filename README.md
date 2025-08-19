# Foro Hub (Challenger Alura) — Spring Boot 3 + JWT + MySQL

Proyecto de práctica estilo “Foro Hub” del curso de Alura/Oracle, implementado con **Spring Boot 3**, **Spring Security (JWT)**, **JPA/Hibernate**, **Flyway** y **MySQL**. Incluye un CRUD de *Tópicos* y autenticación vía **token JWT**.

> **Estado**: funcional con lo mínimo indispensable del curso (login con JWT y CRUD de tópicos).


## 🧭 Índice

- [Arquitectura y paquetes](#arquitectura-y-paquetes)
- [Tecnologías](#tecnologías)
- [Modelo de datos](#modelo-de-datos)
- [Seguridad (JWT)](#seguridad-jwt)
- [Endpoints](#endpoints)
- [Manejo de errores](#manejo-de-errores)
- [Configuración y ejecución](#configuración-y-ejecución)
- [Probar autenticación](#probar-autenticación)
- [Notas y mejoras futuras](#notas-y-mejoras-futuras)


## Arquitectura y paquetes

Organización de paquetes (resumen):

```
com.forohubalura.forohubalura
├── configuraciones   # Seguridad, filtros y handlers globales
├── controller        # Controladores REST (endpoints)
├── DTO               # Data Transfer Objects (request/response)
├── excepciones       # Excepciones de dominio y handlers
├── modelo            # Entidades JPA
├── repository        # Repositorios JPA
└── service           # Servicios de dominio/autenticación
```

**Puntos clave**

- **Controladores**: `TopicoController` (CRUD) y `AuthenticationController` (login).
- **Seguridad**: filtro `SecurityFilter` (extrae y valida JWT) y `TokenService` (genera/valida tokens).
- **Persistencia**: `TopicoRepository` y `UsuarioRepository` (JPA).
- **Servicios**: `TopicoService` (negocio de tópicos) y `AutenticacionService` (UserDetailsService para Security).
- **Errores**: `GlobalExceptionHandler` centraliza respuestas de error legibles.


## Tecnologías

- **Java 17**, **Spring Boot 3.5.x**
- **Spring Web**, **Spring Data JPA**, **Spring Security**
- **Auth0 Java JWT** (firma/validación de tokens)
- **MySQL 8** (con `docker-compose` opcional)
- **Flyway** para migraciones
- **Lombok** para reducir boilerplate


## Modelo de datos

### Usuario
- Campos: `id`, `login`, `contraseña` (hash Bcrypt).
- Implementa `UserDetails` y, por simplicidad, expone `ROLE_USER` por defecto.
- Se utiliza para autenticación y para poblar el `SecurityContext`.

### Tópico
- Campos: `id`, `titulo`, `mensaje`, `creado (fecha_creacion)`, `status`, `autor`, `curso`.
- CRUD completo vía `TopicoController` y `TopicoService`.

> **Nota**: las relaciones Usuario↔Tópico aún no están modeladas (se puede agregar `@ManyToOne` en Tópico cuando se necesite asociar autoría realmente).


## Seguridad (JWT)

### Flujo
1. **POST `/login`** con `{ "login": "...", "contraseña": "..." }`.
2. Spring Security autentica contra la base (via `AuthenticationManager` y `AutenticacionService`).
3. Si es válido, `TokenService` genera un **JWT** (HS256) con `issuer`, `subject` (login) y `exp` (expiración).
4. El cliente envía ese token en cada request protegida usando el header:  
   `Authorization: Bearer <token>`
5. **`SecurityFilter`** intercepta cada petición, extrae el token, lo valida y, si es correcto, setea la autenticación en el `SecurityContext`.

### Componentes
- **`TokenService`**: encapsula la emisión y verificación del token. Usa una *secret key* configurable y expiración.
- **`SecurityFilter`** (OncePerRequestFilter): lee `Authorization`, valida JWT, carga el usuario y crea el `UsernamePasswordAuthenticationToken` en el contexto.
- **`AuthenticationController`**: endpoint público de login que retorna el token.

> **Importante**: asegúrate de usar **la misma clave secreta** al **firmar** y **verificar** el token (ver sección de *Notas y mejoras*).


## Endpoints

### Autenticación
- `POST /login`  
  **Body (JSON)**:
  ```json
  {
    "login": "tu_usuario",
    "contraseña": "tu_clave"
  }
  ```
  **Respuesta**:
  ```json
  {
    "tokenJWT": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

### Tópicos (`/topico`)
> **Protegidos con JWT** (enviar `Authorization: Bearer <token>`)

- `POST /topico` — Crea un tópico (valida DTO)
- `GET /topico` — Lista todos los tópicos
- `GET /topico/{id}` — Recupera por id
- `PUT /topico/{id}` — Actualiza por id
- `DELETE /topico/{id}` — Elimina por id

> La API utiliza DTOs para entrada/salida y responde con códigos adecuados (`201 Created`, `200 OK`, `204 No Content`, `404 Not Found`, `400 Bad Request`, etc.).


## Manejo de errores

- Validaciones de DTO: respuesta `400` con mapa de errores por campo.
- Integridad/Duplicados: respuesta `400` con mensaje claro.
- Recurso no encontrado: respuesta `404` con mensaje.
- Otras violaciones de constraints: `406` según el caso.

Los errores se centralizan en un `@RestControllerAdvice` para devolver mensajes consistentes y útiles.


## Configuración y ejecución

### 1) Base de datos (opcional con Docker)
`docker-compose.yml` mínimo para levantar MySQL 8 con base `foro_hub`:
```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: mysql_foro_hub
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: dario
      MYSQL_DATABASE: foro_hub
      MYSQL_USER: dario
      MYSQL_PASSWORD: dario
    ports:
      - "0.0.0.0:3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-pdario"]
      interval: 10s
      retries: 5
      start_period: 20s

volumes:
  mysql_data:
```

Levantar:
```bash
docker compose up -d
```

### 2) Propiedades de aplicación
Archivo `application.properties` (ejemplo):
```properties
spring.application.name=forohubalura

# MySQL local
spring.datasource.url=jdbc:mysql://localhost:3306/foro_hub
spring.datasource.username=root
spring.datasource.password=dario
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

# Validar esquema vs entidades (Flyway controla el schema)
spring.jpa.hibernate.ddl-auto=validate

# Logs SQL
spring.jpa.show-sql=true

# JWT
api.security.token.secret=${JWT_SECRET:12345678}
api.security.jwt.secret=${JWT_SECRET:12345678}
api.security.jwt.expiration=${JWT_EXPIRACION:5}
```

> **Sugerencia**: exportar `JWT_SECRET` en tu entorno para no hardcodear secretos.


### 3) Migraciones (Flyway)
Colocar los scripts en `src/main/resources/db/migration` con nombres tipo:
```
V1__crear_tablas_iniciales.sql
V2__agregar_indices.sql
...
```
Asegurate de crear las tablas `usuarios` y `topico` consistentes con las entidades.


### 4) Ejecutar la aplicación
Con Maven:
```bash
./mvnw spring-boot:run
# o
mvn spring-boot:run
```


## Probar autenticación

1) **Crear un usuario** en la tabla `usuarios` con contraseña **Bcrypt** (no texto plano).  
   Podés generar hashes Bcrypt desde código (Spring `BCryptPasswordEncoder`) o cualquier herramienta confiable.

2) **Hacer login**:
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"miusuario","contraseña":"123456"}'
```

3) **Usar el token** en endpoints protegidos:
```bash
curl http://localhost:8080/topico \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```


## Notas y mejoras futuras

- **Clave secreta en verificación**: asegurar que `TokenService` use **la misma** secret para *firmar* y *verificar* (no hardcodear `"secret"` en la verificación).
- **Expiración configurable**: parametrizar la expiración leyendo `api.security.jwt.expiration` en `TokenService` para que no quede fija (ej. 2 horas).
- **Repositorio de usuario**: preferir `Optional<Usuario> findByLogin(String login)` y manejar `orElseThrow` para evitar `null`.
- **DTOs y nombres**: considerar no usar campos con caracteres especiales en JSON (ej. `contraseña`) para facilitar clientes; alternativa: `password`.
- **Asociar Tópico a Usuario**: agregar relación `@ManyToOne` en `Topico` y usar el usuario autenticado (`SecurityContextHolder`) como autor real.
- **Respuestas DELETE**: estandarizar a `ResponseEntity<Void>` para `204 No Content`.
- **Lombok**: evitar anotar simultáneamente `@Data` y `@Getter/@Setter` en una misma entidad.
- **Tests**: agregar tests de seguridad con `spring-security-test` y de repositorios/servicios.


---

Hecho con ❤️ para practicar Spring Boot, seguridad con JWT y buenas prácticas de API REST. Con estrecha colaboracion de ChatGPT.