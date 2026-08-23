package co.com.franquicias.usecase.franquicias;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNegocio;
import co.com.franquicias.model.franquicia.Franquicia;
import co.com.franquicias.model.franquicia.gateways.FranquiciaRepositorio;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearFranquiciaCasoDeUso {

    private final FranquiciaRepositorio franquiciaRepositorio;

    public Mono<Franquicia> ejecutar(Franquicia franquicia) {
        return franquiciaRepositorio.existePorTipoDocumentoYNumeroDocumento(
                        franquicia.getTipoDocumento(), franquicia.getNumeroDocumento())
                .flatMap(existe -> existe
                        ? Mono.<Franquicia>error(new ExcepcionNegocio(ErrorDominio.FRANQUICIA_YA_EXISTE))
                        : franquiciaRepositorio.guardar(franquicia));
    }
}
