package co.com.franquicias.usecase.sucursales;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.sucursal.Sucursal;
import co.com.franquicias.model.sucursal.gateways.SucursalRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarNombreSucursalCasoDeUso {

    private final SucursalRepositorio sucursalRepositorio;

    public Mono<Sucursal> ejecutar(Long sucursalId, String nombre) {
        return sucursalRepositorio.buscarPorId(sucursalId)
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.SUCURSAL_NO_ENCONTRADA)))
                .doOnNext(sucursal -> sucursal.setNombre(nombre))
                .flatMap(sucursalRepositorio::guardar);
    }
}
