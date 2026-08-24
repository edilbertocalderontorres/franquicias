package co.com.franquicias.usecase.productos;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarNombreProductoCasoDeUso {

    private final ProductoRepositorio productoRepositorio;

    public Mono<Producto> ejecutar(Long productoId, String nombre) {
        return productoRepositorio.buscarPorId(productoId)
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.PRODUCTO_NO_ENCONTRADO)))
                .doOnNext(producto -> producto.setNombre(nombre))
                .flatMap(productoRepositorio::guardar);
    }
}
