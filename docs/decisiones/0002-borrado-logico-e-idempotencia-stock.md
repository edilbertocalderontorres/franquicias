# Borrado lógico de producto e idempotencia en stock

Eliminar un producto no borra la fila: solo pone activo=false. Borrado físico pierde trazabilidad y rompe cualquier referencia futura desde transaccion_inventario. El código no se libera (mismo criterio que en la 0001). buscarPorId y cualquier lectura de negocio filtran activo=true, así que un producto borrado se ve como "no encontrado" para el resto del sistema.

Actualizar stock es el caso interesante: el endpoint puede recibir la misma petición más de una vez (reintento de red, doble clic del usuario), y sin protección eso aplica el delta dos veces.

**Enfoque: delta relativo, no valor absoluto.** El cliente manda cuánto cambia (+5, -3) y la aritmética pasa dentro de un solo UPDATE con bloqueo de fila:

```sql
UPDATE producto SET stock = stock + :delta
WHERE id = :id AND activo = true AND stock + :delta >= 0
```

Sin lectura previa, sin lost update, y el propio WHERE rechaza cualquier delta que deje el stock negativo (0 filas afectadas = stock insuficiente).

El delta solo no es idempotente: reenviar la misma petición reaplica el delta. Por eso el cliente manda una Idempotency-Key por header — un id por intención de operación de negocio, no por intento HTTP. Todos los reintentos de la misma intención reenvían la misma key. Es única globalmente, no compuesta con producto_id: una intención siempre es sobre un solo producto. Si la key ya se procesó, no se reaplica el delta: se devuelve el estado actual del producto, como si hubiera funcionado.

El usuario que originó el movimiento también va por header (X-Usuario) — no hay autenticación todavía; cuando la haya, esto se reemplaza por el subject del token.

transaccion_inventario guarda el control: idkey (unique), producto_id, delta, usuario, fecha_creación. No guardo el stock resultante — como el delta se aplica una sola vez, el stock actual del producto ya es la respuesta correcta para un replay.

El UPDATE del stock y el INSERT de la transacción van en la misma transacción. Si el INSERT choca por idkey duplicada (carrera entre dos peticiones concurrentes con la misma key), todo revierte y se responde con el estado actual — mismo resultado que un replay normal.

No expongo transaccion_inventario como concepto de dominio: el caso de uso solo conoce ProductoRepositorio.aplicarMovimientoStock(...); cómo se garantiza la idempotencia es un detalle de persistencia.
