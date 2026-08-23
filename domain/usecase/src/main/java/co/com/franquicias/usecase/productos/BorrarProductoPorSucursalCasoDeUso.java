package co.com.franquicias.usecase.productos;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BorrarProductoPorSucursalCasoDeUso {

    private final ProductoRepositorio productoRepositorio;

    public Mono<Void> ejecutar(Long sucursalId, Long productoId) {
        return productoRepositorio.buscarPorId(productoId)
                .filter(producto -> producto.getSucursalId().equals(sucursalId))
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.PRODUCTO_NO_ENCONTRADO)))
                .flatMap(producto -> productoRepositorio.eliminarLogicamente(productoId));
    }
}
