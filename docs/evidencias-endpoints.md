# Evidencias de pruebas manuales — API Franquicias

Pruebas ejecutadas contra el stack levantado con `docker compose -f docs/docker-compose.yml up -d --build` (Postgres + app en contenedores), vía `curl` directo a `http://localhost:8080`. Fecha: 2026-08-23.

Durante esta ronda de pruebas aparecieron y se corrigieron 3 problemas reales que solo se manifestaban corriendo la app de verdad (no los detectaban los tests unitarios, que mockean las dependencias):

1. **`ObjectMapperConfig` faltante** — el bean `ObjectMapper` que usan los adaptadores r2dbc no estaba definido en `app-service` (se había borrado en algún punto, dejando un test huérfano). Sin él la app no arrancaba.
2. **`WebProperties.Resources` no es un bean inyectable** en esta versión de Spring Boot (4.1.1) — solo `WebProperties` completo lo es. Se corrigió el constructor de `ManejadorGlobalErroresWeb` para inyectar `WebProperties` y extraer `.getResources()`.
3. **Ruta no registrada devolvía 500 en vez de 404** — el manejador global no reconocía `ResponseStatusException` (la excepción que lanza WebFlux cuando ninguna ruta matchea). Se agregó ese caso, extrayendo el status real de la excepción.

También se corrigió el `Dockerfile` (referenciaba un usuario `appuser` que nunca se creaba, y traía una flag de JVM de OpenJ9 que no existe en Temurin/HotSpot).

## Resumen de casos

| # | Caso | Método y ruta | Esperado | Obtenido |
|---|------|----------------|----------|----------|
| 1 | Crear franquicia | POST /api/franquicias | 201 | ✅ 201 |
| 2 | Franquicia duplicada (mismo documento) | POST /api/franquicias | 409 | ✅ 409 |
| 3 | Crear sucursal | POST /api/franquicias/1/sucursales | 201 | ✅ 201 |
| 4 | Sucursal en franquicia inexistente | POST /api/franquicias/9999/sucursales | 404 | ✅ 404 |
| 5 | Sucursal duplicada (mismo código) | POST /api/franquicias/1/sucursales | 409 | ✅ 409 |
| 6 | Crear segunda sucursal | POST /api/franquicias/1/sucursales | 201 | ✅ 201 |
| 7 | Crear producto (stock alto) | POST /api/sucursales/1/productos | 201 | ✅ 201 |
| 8 | Crear producto (stock bajo) | POST /api/sucursales/1/productos | 201 | ✅ 201 |
| 9 | Crear producto en sucursal 2 | POST /api/sucursales/2/productos | 201 | ✅ 201 |
| 10 | Producto en sucursal inexistente | POST /api/sucursales/9999/productos | 404 | ✅ 404 |
| 11 | Producto duplicado (mismo código) | POST /api/sucursales/1/productos | 409 | ✅ 409 |
| 12 | Mayor stock por sucursal (criterio 7) | GET /api/franquicias/1/productos/mayor-stock | 200, elige el de más stock por sucursal | ✅ 200 |
| 13 | Mayor stock, franquicia inexistente | GET /api/franquicias/9999/productos/mayor-stock | 404 | ✅ 404 |
| 14 | Actualizar stock, delta positivo | PATCH /api/productos/2/stock | 200, stock 20→25 | ✅ 200 |
| 15 | Reintento con la misma Idempotency-Key | PATCH /api/productos/2/stock | 200, stock **no** vuelve a cambiar | ✅ 200, stock sigue en 25 |
| 16 | Actualizar stock, delta negativo válido | PATCH /api/productos/2/stock | 200, stock 25→15 | ✅ 200 |
| 17 | Delta que deja stock negativo | PATCH /api/productos/2/stock | 409 | ✅ 409 |
| 18 | Falta header Idempotency-Key | PATCH /api/productos/2/stock | 400 | ✅ 400 |
| 19 | Falta header X-Usuario | PATCH /api/productos/2/stock | 400 | ✅ 400 |
| 20 | Actualizar stock, producto inexistente | PATCH /api/productos/9999/stock | 404 | ✅ 404 |
| 21 | Eliminar producto (borrado lógico) | DELETE /api/sucursales/1/productos/1 | 204 | ✅ 204 |
| 22 | Eliminar el mismo producto otra vez | DELETE /api/sucursales/1/productos/1 | 404 | ✅ 404 |
| 23 | Eliminar producto con sucursal del path incorrecta | DELETE /api/sucursales/2/productos/2 | 404 | ✅ 404 |
| 24 | Mayor stock tras el borrado | GET /api/franquicias/1/productos/mayor-stock | Ya no debe salir el producto borrado | ✅ Cambió a "Gorra" |
| 25 | Actualizar stock de producto ya borrado | PATCH /api/productos/1/stock | 404 | ✅ 404 |
| 26 | Verificación en BD: `producto` | `docker exec` + `psql` | activo=false y código no reutilizado | ✅ |
| 27 | Verificación en BD: `transaccion_inventario` | `docker exec` + `psql` | 2 filas (no 3: el reintento y el fallido no insertan) | ✅ |
| 28 | Ruta no registrada | GET /api/ruta-que-no-existe | 404 | ✅ 404 (tras el fix) |

## Datos de prueba creados

- Franquicia 1: *Franquicia Andina* (NIT 900123456)
- Sucursal 1: *Sucursal Centro* (SUC-001) — Sucursal 2: *Sucursal Norte* (SUC-002)
- Producto 1: *Camiseta Basica* (PRD-001, stock 50, sucursal 1) — eliminado en el caso 21
- Producto 2: *Gorra* (PRD-002, stock 20→25→15, sucursal 1)
- Producto 3: *Pantalon* (PRD-003, stock 15, sucursal 2)

## Detalle de request/response por caso

### 1. Crear franquicia (éxito)
```
POST http://localhost:8080/api/franquicias
Body: {"nombre":"Franquicia Andina","tipoDocumento":"NIT","numeroDocumento":"900123456"}

HTTP/1.1 201 Created
Content-Type: application/json

{"id":1,"nombre":"Franquicia Andina","numeroDocumento":"900123456","tipoDocumento":"NIT"}
```

### 2. Crear franquicia duplicada (mismo tipoDocumento+numeroDocumento) → 409
```
POST http://localhost:8080/api/franquicias
Body: {"nombre":"Otra Razon Social","tipoDocumento":"NIT","numeroDocumento":"900123456"}

HTTP/1.1 409 Conflict
Content-Type: application/json

{"error":"Conflict","estado":409,"fecha":"2026-08-23T17:10:42.807192778Z","mensaje":"Ya existe una franquicia con ese tipo y número de documento","ruta":"/api/franquicias"}
```

### 3. Crear sucursal en franquicia existente (éxito)
```
POST http://localhost:8080/api/franquicias/1/sucursales
Body: {"nombre":"Sucursal Centro","codigo":"SUC-001"}

HTTP/1.1 201 Created
Content-Type: application/json

{"codigo":"SUC-001","franquiciaId":1,"id":1,"nombre":"Sucursal Centro"}
```

### 4. Crear sucursal en franquicia inexistente → 404
```
POST http://localhost:8080/api/franquicias/9999/sucursales
Body: {"nombre":"Sucursal Fantasma","codigo":"SUC-XXX"}

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:10:53.304939394Z","mensaje":"La franquicia solicitada no existe","ruta":"/api/franquicias/9999/sucursales"}
```

### 5. Crear sucursal duplicada (mismo código en la misma franquicia) → 409
```
POST http://localhost:8080/api/franquicias/1/sucursales
Body: {"nombre":"Sucursal Centro 2","codigo":"SUC-001"}

HTTP/1.1 409 Conflict
Content-Type: application/json

{"error":"Conflict","estado":409,"fecha":"2026-08-23T17:10:53.489903175Z","mensaje":"Ya existe una sucursal con ese código en la franquicia","ruta":"/api/franquicias/1/sucursales"}
```

### 6. Crear segunda sucursal (para el criterio 7)
```
POST http://localhost:8080/api/franquicias/1/sucursales
Body: {"nombre":"Sucursal Norte","codigo":"SUC-002"}

HTTP/1.1 201 Created
Content-Type: application/json

{"codigo":"SUC-002","franquiciaId":1,"id":2,"nombre":"Sucursal Norte"}
```

### 7. Crear producto — stock alto (éxito)
```
POST http://localhost:8080/api/sucursales/1/productos
Body: {"nombre":"Camiseta Basica","codigo":"PRD-001","stock":50}

HTTP/1.1 201 Created
Content-Type: application/json

{"codigo":"PRD-001","id":1,"nombre":"Camiseta Basica","stock":50,"sucursalId":1}
```

### 8. Crear segundo producto en la misma sucursal — stock bajo
```
POST http://localhost:8080/api/sucursales/1/productos
Body: {"nombre":"Gorra","codigo":"PRD-002","stock":20}

HTTP/1.1 201 Created
Content-Type: application/json

{"codigo":"PRD-002","id":2,"nombre":"Gorra","stock":20,"sucursalId":1}
```

### 9. Crear producto en sucursal 2
```
POST http://localhost:8080/api/sucursales/2/productos
Body: {"nombre":"Pantalon","codigo":"PRD-003","stock":15}

HTTP/1.1 201 Created
Content-Type: application/json

{"codigo":"PRD-003","id":3,"nombre":"Pantalon","stock":15,"sucursalId":2}
```

### 10. Crear producto en sucursal inexistente → 404
```
POST http://localhost:8080/api/sucursales/9999/productos
Body: {"nombre":"Producto Fantasma","codigo":"PRD-XXX","stock":1}

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:11:07.648765363Z","mensaje":"La sucursal solicitada no existe","ruta":"/api/sucursales/9999/productos"}
```

### 11. Crear producto duplicado (mismo código en la misma sucursal) → 409
```
POST http://localhost:8080/api/sucursales/1/productos
Body: {"nombre":"Camiseta Basica v2","codigo":"PRD-001","stock":5}

HTTP/1.1 409 Conflict
Content-Type: application/json

{"error":"Conflict","estado":409,"fecha":"2026-08-23T17:11:07.883233481Z","mensaje":"Ya existe un producto con ese código en la sucursal","ruta":"/api/sucursales/1/productos"}
```

### 12. Criterio 7 — producto con mayor stock por sucursal (éxito)
```
GET http://localhost:8080/api/franquicias/1/productos/mayor-stock

HTTP/1.1 200 OK
Content-Type: application/json

[{"productoId":3,"productoNombre":"Pantalon","stock":15,"sucursalId":2,"sucursalNombre":"Sucursal Norte"},
 {"productoId":1,"productoNombre":"Camiseta Basica","stock":50,"sucursalId":1,"sucursalNombre":"Sucursal Centro"}]
```
Confirma la lógica: en Sucursal Centro elige "Camiseta Basica" (50) sobre "Gorra" (20).

### 13. Criterio 7 en franquicia inexistente → 404
```
GET http://localhost:8080/api/franquicias/9999/productos/mayor-stock

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:11:17.982806369Z","mensaje":"La franquicia solicitada no existe","ruta":"/api/franquicias/9999/productos/mayor-stock"}
```

### 14. Actualizar stock — delta positivo (éxito)
```
PATCH http://localhost:8080/api/productos/2/stock
Headers: Idempotency-Key: idem-001, X-Usuario: edilberto
Body: {"delta":5}

HTTP/1.1 200 OK
Content-Type: application/json

{"codigo":"PRD-002","id":2,"nombre":"Gorra","stock":25,"sucursalId":1}
```

### 15. Reenviar exactamente la misma Idempotency-Key (reintento de red) — no reaplica el delta
```
PATCH http://localhost:8080/api/productos/2/stock
Headers: Idempotency-Key: idem-001, X-Usuario: edilberto
Body: {"delta":5}

HTTP/1.1 200 OK
Content-Type: application/json

{"codigo":"PRD-002","id":2,"nombre":"Gorra","stock":25,"sucursalId":1}
```
El stock sigue en 25 (no en 30) — el delta no se volvió a aplicar.

### 16. Actualizar stock — delta negativo válido (éxito)
```
PATCH http://localhost:8080/api/productos/2/stock
Headers: Idempotency-Key: idem-002, X-Usuario: edilberto
Body: {"delta":-10}

HTTP/1.1 200 OK
Content-Type: application/json

{"codigo":"PRD-002","id":2,"nombre":"Gorra","stock":15,"sucursalId":1}
```

### 17. Delta que dejaría el stock negativo → 409 STOCK_INSUFICIENTE
```
PATCH http://localhost:8080/api/productos/2/stock
Headers: Idempotency-Key: idem-003, X-Usuario: edilberto
Body: {"delta":-999}

HTTP/1.1 409 Conflict
Content-Type: application/json

{"error":"Conflict","estado":409,"fecha":"2026-08-23T17:11:33.670500702Z","mensaje":"El stock resultante no puede ser negativo","ruta":"/api/productos/2/stock"}
```

### 18. Actualizar stock sin header Idempotency-Key → 400
```
PATCH http://localhost:8080/api/productos/2/stock
Headers: X-Usuario: edilberto (sin Idempotency-Key)
Body: {"delta":1}

HTTP/1.1 400 Bad Request
Content-Type: application/json

{"error":"Bad Request","estado":400,"fecha":"2026-08-23T17:11:46.670657209Z","mensaje":"Falta el header requerido: Idempotency-Key","ruta":"/api/productos/2/stock"}
```

### 19. Actualizar stock sin header X-Usuario → 400
```
PATCH http://localhost:8080/api/productos/2/stock
Headers: Idempotency-Key: idem-004 (sin X-Usuario)
Body: {"delta":1}

HTTP/1.1 400 Bad Request
Content-Type: application/json

{"error":"Bad Request","estado":400,"fecha":"2026-08-23T17:11:46.937436964Z","mensaje":"Falta el header requerido: X-Usuario","ruta":"/api/productos/2/stock"}
```

### 20. Actualizar stock de producto inexistente → 404
```
PATCH http://localhost:8080/api/productos/9999/stock
Headers: Idempotency-Key: idem-005, X-Usuario: edilberto
Body: {"delta":1}

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:11:47.205263597Z","mensaje":"El producto solicitado no existe","ruta":"/api/productos/9999/stock"}
```

### 21. Eliminar (borrado lógico) el producto de mayor stock de Sucursal Centro (éxito)
```
DELETE http://localhost:8080/api/sucursales/1/productos/1

HTTP/1.1 204 No Content
```

### 22. Eliminar el mismo producto otra vez → 404
```
DELETE http://localhost:8080/api/sucursales/1/productos/1

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:12:00.477177873Z","mensaje":"El producto solicitado no existe","ruta":"/api/sucursales/1/productos/1"}
```

### 23. Eliminar un producto que existe pero en otra sucursal (mismatch de path) → 404
```
DELETE http://localhost:8080/api/sucursales/2/productos/2

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:12:00.645864411Z","mensaje":"El producto solicitado no existe","ruta":"/api/sucursales/2/productos/2"}
```
El producto 2 existe (es "Gorra"), pero pertenece a la sucursal 1, no a la 2 — por eso 404 y no un borrado accidental.

### 24. Criterio 7 tras el borrado — ya no debe salir el producto eliminado
```
GET http://localhost:8080/api/franquicias/1/productos/mayor-stock

HTTP/1.1 200 OK
Content-Type: application/json

[{"productoId":2,"productoNombre":"Gorra","stock":15,"sucursalId":1,"sucursalNombre":"Sucursal Centro"},
 {"productoId":3,"productoNombre":"Pantalon","stock":15,"sucursalId":2,"sucursalNombre":"Sucursal Norte"}]
```
Sucursal Centro ahora muestra "Gorra" (el producto activo restante), no la "Camiseta Basica" que se eliminó.

### 25. Actualizar stock de un producto ya eliminado → 404
```
PATCH http://localhost:8080/api/productos/1/stock
Headers: Idempotency-Key: idem-006, X-Usuario: edilberto
Body: {"delta":1}

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:12:01.050295193Z","mensaje":"El producto solicitado no existe","ruta":"/api/productos/1/stock"}
```

### 26. Verificación directa en BD — tabla `producto`
```
$ docker exec franquicias-postgres psql -U app -d franquicias -c \
  "SELECT id, nombre, codigo, stock, activo, sucursal_id FROM producto ORDER BY id;"

 id |     nombre      | codigo  | stock | activo | sucursal_id
----+-----------------+---------+-------+--------+-------------
  1 | Camiseta Basica | PRD-001 |    50 | f      |           1
  2 | Gorra           | PRD-002 |    15 | t      |           1
  3 | Pantalon        | PRD-003 |    15 | t      |           2
```
El producto 1 sigue en la tabla (no se borró físicamente), `activo=f`, y su código `PRD-001` queda "usado" — no se libera para otro producto de esa sucursal.

### 27. Verificación directa en BD — tabla `transaccion_inventario`
```
$ docker exec franquicias-postgres psql -U app -d franquicias -c \
  "SELECT idkey, producto_id, delta, usuario, fecha_creacion FROM transaccion_inventario ORDER BY id;"

  idkey   | producto_id | delta |  usuario  |        fecha_creacion
----------+-------------+-------+-----------+-------------------------------
 idem-001 |           2 |     5 | edilberto | 2026-08-23 17:11:32.817128+00
 idem-002 |           2 |   -10 | edilberto | 2026-08-23 17:11:33.408339+00
```
Solo 2 filas: el reintento con `idem-001` (caso 15) no insertó una fila nueva, y el intento fallido `idem-003` (stock insuficiente, caso 17) tampoco quedó registrado — la transacción se revirtió completa.

### 28. Ruta no registrada → 404
```
GET http://localhost:8080/api/ruta-que-no-existe

HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"Not Found","estado":404,"fecha":"2026-08-23T17:13:35.731397306Z","mensaje":"404 NOT_FOUND \"No static resource api/ruta-que-no-existe for request 'http://localhost:8080/api/ruta-que-no-existe'.\"","ruta":"/api/ruta-que-no-existe"}
```
Antes del fix del manejador global, esto devolvía **500** en vez de 404 (ver nota al inicio del documento).
