package co.com.franquicias.usecase.franquicias;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.franquicia.Franquicia;
import co.com.franquicias.model.franquicia.gateways.FranquiciaRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarNombreFranquiciaCasoDeUso {

    private final FranquiciaRepositorio franquiciaRepositorio;

    public Mono<Franquicia> ejecutar(Long franquiciaId, String nombre) {
        return franquiciaRepositorio.buscarPorId(franquiciaId)
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.FRANQUICIA_NO_ENCONTRADA)))
                .doOnNext(franquicia -> franquicia.setNombre(nombre))
                .flatMap(franquiciaRepositorio::guardar);
    }
}
