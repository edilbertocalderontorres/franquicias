package co.com.franquicias.model.producto.gateways;

import co.com.franquicias.model.producto.Producto;
import reactor.core.publisher.Mono;

public interface ProductoRepositorio {

    Mono<Producto> guardar(Producto producto);

    Mono<Producto> buscarPorId(Long id);

    Mono<Void> eliminarLogicamente(Long id);

    Mono<Boolean> existePorSucursalIdYCodigo(Long sucursalId, String codigo);

    Mono<Producto> aplicarMovimientoStock(Long productoId, String idempotencyKey, String usuario, Integer delta);

    Mono<Producto> buscarMayorStockPorSucursal(Long sucursalId);
}
