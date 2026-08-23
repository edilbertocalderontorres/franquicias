package co.com.franquicias.usecase.consultasnegocio;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.franquicia.gateways.FranquiciaRepositorio;
import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import co.com.franquicias.model.sucursal.Sucursal;
import co.com.franquicias.model.sucursal.gateways.SucursalRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@RequiredArgsConstructor
public class ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso {

    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;

    public Flux<Tuple2<Producto, Sucursal>> ejecutar(Long franquiciaId) {
        return franquiciaRepositorio.buscarPorId(franquiciaId)
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.FRANQUICIA_NO_ENCONTRADA)))
                .flatMapMany(franquicia -> sucursalRepositorio.buscarPorFranquiciaId(franquiciaId))
                .flatMap(sucursal -> productoRepositorio.buscarMayorStockPorSucursal(sucursal.getId())
                        .map(producto -> Tuples.of(producto, sucursal)));
    }
}
