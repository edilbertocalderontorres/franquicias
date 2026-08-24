# Ejemplos para probar la API

Requests de ejemplo contra `http://localhost:8080` (ver [`como-levantar-en-local.md`](como-levantar-en-local.md) para levantar el servicio).

## Crear franquicia

```
curl -i -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Franquicia Andina","tipoDocumento":"NIT","numeroDocumento":"900123456"}'
```
→ `201` con la franquicia creada. `409` si ya existe una con ese `tipoDocumento`+`numeroDocumento`.

## Actualizar nombre de franquicia

```
curl -i -X PATCH http://localhost:8080/api/franquicias/1/nombre \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Franquicia Andina Renombrada"}'
```
→ `200` con la franquicia actualizada. `404` si no existe.

## Crear sucursal

```
curl -i -X POST http://localhost:8080/api/franquicias/1/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Centro","codigo":"SUC-001"}'
```
→ `201`. `404` si la franquicia no existe, `409` si el `codigo` ya está usado en esa franquicia.

## Actualizar nombre de sucursal

```
curl -i -X PATCH http://localhost:8080/api/sucursales/1/nombre \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Centro Renombrada"}'
```
→ `200` con la sucursal actualizada. `404` si no existe.

## Crear producto

```
curl -i -X POST http://localhost:8080/api/sucursales/1/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Camiseta Basica","codigo":"PRD-001","stock":50}'
```
→ `201`. `404` si la sucursal no existe, `409` si el `codigo` ya está usado en esa sucursal.

## Eliminar producto (borrado lógico)

```
curl -i -X DELETE http://localhost:8080/api/sucursales/1/productos/1
```
→ `204`. `404` si no existe, ya está eliminado, o pertenece a otra sucursal.

## Actualizar stock (idempotente)

`Idempotency-Key` identifica la *intención* de la operación: reenviar la misma key en un reintento de red no vuelve a aplicar el delta.

```
curl -i -X PATCH http://localhost:8080/api/productos/1/stock \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: $(uuidgen)" \
  -H "X-Usuario: edilberto" \
  -d '{"delta":5}'
```
→ `200` con el producto actualizado. `400` si falta algún header. `404` si el producto no existe. `409` si el delta deja el stock negativo. Para probar un decremento, usar `{"delta":-5}`.

## Actualizar nombre de producto

```
curl -i -X PATCH http://localhost:8080/api/productos/1/nombre \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Camiseta Basica Renombrada"}'
```
→ `200` con el producto actualizado. `404` si no existe.

## Producto con más stock por sucursal (franquicia puntual)

```
curl -i http://localhost:8080/api/franquicias/1/productos/mayor-stock
```
→ `200` con un producto por cada sucursal de la franquicia (el de mayor stock activo). `404` si la franquicia no existe.
