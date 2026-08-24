# Franquicias API

API reactiva para administrar franquicias, sus sucursales y los productos ofertados en cada una, con control de stock idempotente.

**Cómo levantarlo en local:** [`docs/como-levantar-en-local.md`](docs/como-levantar-en-local.md).

## Arquitectura

- Generado con el plugin de **arquitectura limpia de Bancolombia** (`co.com.bancolombia.cleanArchitecture`).
- **WebFlux + Project Reactor** de punta a punta (entry-point funcional con `RouterFunction`/`HandlerFunction`, persistencia con R2DBC) — nada bloqueante.
- Módulos:

  | Módulo | Contiene |
  |---|---|
  | `domain/model` | Entidades y puertos (gateways), sin dependencias de framework |
  | `domain/usecase` | Casos de uso (`*CasoDeUso`), orquestan los gateways |
  | `infrastructure/entry-points/reactive-web` | Router, handlers segmentados por entidad (`ManejadorFranquicia`, `ManejadorSucursal`, `ManejadorProducto`), DTOs, mapeo con MapStruct, manejo global de errores |
  | `infrastructure/driven-adapters/r2dbc-postgresql` | Adaptadores de persistencia (Postgres reactivo) |
  | `applications/app-service` | Ensambla todo, config de Spring Boot |

## Modelo de datos

`Franquicia (1) → Sucursal (N) → Producto (N)`, más `transaccion_inventario` como tabla de control de idempotencia para movimientos de stock (no es un concepto de dominio, es un detalle de persistencia).

`nombre` **nunca** es clave de unicidad (un espacio o una mayúscula de más genera duplicados accidentales de la misma entidad). La unicidad real es:

- **Franquicia** → `tipoDocumento` + `numeroDocumento` (es una entidad legal real).
- **Sucursal** → `codigo` interno, único por franquicia (no tiene documento propio).
- **Producto** → `codigo` interno, único por sucursal (mismo razonamiento que sucursal).

`producto` usa **borrado lógico** (`activo`); un producto eliminado se ve como "no encontrado" para el resto del sistema, y su código no se libera.

Decisiones completas, con alternativas descartadas: [`docs/decisiones/0001-identificador-unico-de-negocio.md`](docs/decisiones/0001-identificador-unico-de-negocio.md) y [`docs/decisiones/0002-borrado-logico-e-idempotencia-stock.md`](docs/decisiones/0002-borrado-logico-e-idempotencia-stock.md). DDL: [`docs/db/schema.sql`](docs/db/schema.sql).

Propuesta de escalamiento a futuro (no implementada): [`docs/decisiones/0003-escalamiento-propuesto-no-implementado.md`](docs/decisiones/0003-escalamiento-propuesto-no-implementado.md).

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/franquicias` | Crear franquicia |
| PATCH | `/api/franquicias/{franquiciaId}/nombre` | Actualizar nombre de franquicia |
| POST | `/api/franquicias/{franquiciaId}/sucursales` | Crear sucursal |
| PATCH | `/api/sucursales/{sucursalId}/nombre` | Actualizar nombre de sucursal |
| POST | `/api/sucursales/{sucursalId}/productos` | Crear producto |
| DELETE | `/api/sucursales/{sucursalId}/productos/{productoId}` | Eliminar producto (borrado lógico) |
| PATCH | `/api/productos/{productoId}/stock` | Actualizar stock por delta, idempotente (headers `Idempotency-Key` y `X-Usuario` requeridos) |
| PATCH | `/api/productos/{productoId}/nombre` | Actualizar nombre de producto |
| GET | `/api/franquicias/{franquiciaId}/productos/mayor-stock` | Producto con más stock por cada sucursal de la franquicia |

Documentación interactiva (OpenAPI): `http://localhost:8080/openapi.yml` — pegar la URL en [editor.swagger.io](https://editor.swagger.io) o importarla en Postman.

Ejemplos de curl (paths y bodies) para probar cada endpoint: [`docs/ejemplos-curl.md`](docs/ejemplos-curl.md).
