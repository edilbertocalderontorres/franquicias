package co.com.franquicias.model.producto.gateways;

import co.com.franquicias.model.producto.Producto;
import reactor.core.publisher.Mono;

public interface ProductoRepositorio {

    Mono<Producto> guardar(Producto producto);

    Mono<Boolean> existePorSucursalIdYCodigo(Long sucursalId, String codigo);
}
