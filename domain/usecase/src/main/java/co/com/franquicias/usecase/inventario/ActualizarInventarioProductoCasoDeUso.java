package co.com.franquicias.usecase.inventario;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarInventarioProductoCasoDeUso {

    private final ProductoRepositorio productoRepositorio;

    public Mono<Producto> ejecutar(Long productoId, String idempotencyKey, String usuario, Integer delta) {
        return productoRepositorio.buscarPorId(productoId)
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.PRODUCTO_NO_ENCONTRADO)))
                .flatMap(producto -> productoRepositorio.aplicarMovimientoStock(productoId, idempotencyKey, usuario, delta));
    }
}
