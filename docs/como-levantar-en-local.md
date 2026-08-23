# Cómo levantar el servicio en local

## Requisitos

- JDK 21+ (el proyecto usa toolchain 25; Gradle lo descarga solo si no lo tenés)
- Docker + Docker Compose

## Pasos

1. Compilar el jar:
   ```
   ./gradlew :app-service:bootJar
   ```
2. Levantar Postgres + la app:
   ```
   docker compose -f docs/docker-compose.yml up -d --build
   ```
3. Verificar que arrancó:
   ```
   curl http://localhost:8080/actuator/health
   ```
4. Ver la documentación de la API: abrir `http://localhost:8080/openapi.yml` y pegar su contenido en [editor.swagger.io](https://editor.swagger.io), o importar esa URL directo en Postman/Insomnia.

## Apagar

```
docker compose -f docs/docker-compose.yml down       # conserva los datos
docker compose -f docs/docker-compose.yml down -v    # borra también el volumen de Postgres
```

## Variables de entorno de la base de datos

Con defaults que ya matchean el `docker-compose.yml` (no hace falta setearlas para correr local):

| Variable | Default local |
|---|---|
| `DB_HOST` | `localhost` (`postgres` dentro del compose) |
| `DB_PORT` | `5432` |
| `DB_NAME` | `franquicias` |
| `DB_SCHEMA` | `public` |
| `DB_USERNAME` | `app` |
| `DB_PASSWORD` | `app` |

## Sin Docker (solo Gradle)

Si ya tenés un Postgres corriendo en otro lado, aplicá `docs/db/schema.sql` a mano y arrancá con:

```
DB_HOST=<host> DB_USERNAME=<user> DB_PASSWORD=<pass> ./gradlew :app-service:bootRun
```

## Evidencia de pruebas

`docs/evidencias-endpoints.md` tiene el request/response real de cada endpoint (éxito y error) probado contra este mismo compose.
