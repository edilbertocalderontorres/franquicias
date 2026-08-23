package co.com.franquicias.model.franquicia.gateways;

import co.com.franquicias.model.franquicia.Franquicia;
import co.com.franquicias.model.franquicia.TipoDocumento;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositorio {

    Mono<Franquicia> guardar(Franquicia franquicia);

    Mono<Boolean> existePorTipoDocumentoYNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);
}
