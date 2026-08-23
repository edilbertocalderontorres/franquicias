# Escalamiento propuesto (no implementado)

Esto es una propuesta de diseño, no algo construido. Nada de lo que sigue existe en el código — el proyecto completo sigue en Postgres, tal como está documentado en la 0001 y la 0002.

El stock es el único dato con perfil de escritura caliente: alta frecuencia, acceso puntual, contención bajo concurrencia. El resto de la estructura (franquicia, sucursal, nombre, código) cambia rara vez.

A escala, lo separaría a un almacén clave-valor (DynamoDB), con MySQL como sistema de registro de la estructura. Partición por franquicia, ordenación `sucursalId#productoId`, sin nombres desnormalizados — así los renombrados no generan escritura en ambas tablas ni deriva.

**Descartado aquí:** el ítem tendría un único atributo propio (el stock). El costo — escritura en ambas tablas sin transacción distribuida, estrategia de compensación, un segundo almacén en el despliegue — no se amortiza a esta escala. La separación se justifica cuando la escritura sobre el contador domina la carga, no por la naturaleza del dato.

**Sobre la escritura en ambas tablas:** `TransactionalOperator` no cubre ambos almacenes — no hay commit coordinado ni rollback en Dynamo. La solución robusta es outbox transaccional; la mínima, ordenar las escrituras para que el fallo deje el estado menos dañino, y hacer las operaciones idempotentes.

---

**Las dos secciones siguientes son propuesta, no el comportamiento actual de la API.** Hoy el criterio 7 corre contra Postgres, con la query real en `ProductoRepositorioReactivo.buscarMayorStockPorSucursal`. Nada de lo de abajo está implementado.

**Propuesta — req. 7 en un escenario con split:** tiene perfil analítico, no crítico: tolera latencia y consistencia eventual. Se resolvería con una proyección de lectura mantenida asíncronamente, no con consulta cruzada en línea contra los dos almacenes.

**Propuesta — sin índice por stock en Dynamo:** sería sort key inmutable (delete+put por cada actualización). El máximo por sucursal se resolvería en memoria después de la query, no con un índice ordenado por stock.
