package co.com.franquicias.usecase.productos;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNegocio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import co.com.franquicias.model.sucursal.gateways.SucursalRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearProductoPorSucursalCasoDeUso {

    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;

    public Mono<Producto> ejecutar(Producto producto) {
        return sucursalRepositorio.buscarPorId(producto.getSucursalId())
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.SUCURSAL_NO_ENCONTRADA)))
                .flatMap(sucursal -> productoRepositorio.existePorSucursalIdYCodigo(
                        producto.getSucursalId(), producto.getCodigo()))
                .flatMap(existe -> existe
                        ? Mono.<Producto>error(new ExcepcionNegocio(ErrorDominio.PRODUCTO_YA_EXISTE))
                        : productoRepositorio.guardar(producto));
    }
}
