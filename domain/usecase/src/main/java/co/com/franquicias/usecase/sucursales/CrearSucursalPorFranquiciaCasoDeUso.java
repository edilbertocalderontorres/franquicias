package co.com.franquicias.usecase.sucursales;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNegocio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.franquicia.gateways.FranquiciaRepositorio;
import co.com.franquicias.model.sucursal.Sucursal;
import co.com.franquicias.model.sucursal.gateways.SucursalRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearSucursalPorFranquiciaCasoDeUso {

    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;

    public Mono<Sucursal> ejecutar(Sucursal sucursal) {
        return franquiciaRepositorio.buscarPorId(sucursal.getFranquiciaId())
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.FRANQUICIA_NO_ENCONTRADA)))
                .flatMap(franquicia -> sucursalRepositorio.existePorFranquiciaIdYCodigo(
                        sucursal.getFranquiciaId(), sucursal.getCodigo()))
                .flatMap(existe -> existe
                        ? Mono.<Sucursal>error(new ExcepcionNegocio(ErrorDominio.SUCURSAL_YA_EXISTE))
                        : sucursalRepositorio.guardar(sucursal));
    }
}
