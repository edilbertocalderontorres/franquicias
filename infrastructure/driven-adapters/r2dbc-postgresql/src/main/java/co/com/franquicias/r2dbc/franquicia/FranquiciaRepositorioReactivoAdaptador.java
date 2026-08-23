package co.com.franquicias.r2dbc.franquicia;

import co.com.franquicias.model.franquicia.Franquicia;
import co.com.franquicias.model.franquicia.TipoDocumento;
import co.com.franquicias.model.franquicia.gateways.FranquiciaRepositorio;
import co.com.franquicias.r2dbc.entity.FranquiciaEntidad;
import co.com.franquicias.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranquiciaRepositorioReactivoAdaptador extends ReactiveAdapterOperations<
        Franquicia,
        FranquiciaEntidad,
        Long,
        FranquiciaRepositorioReactivo
    > implements FranquiciaRepositorio {

    public FranquiciaRepositorioReactivoAdaptador(FranquiciaRepositorioReactivo repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franquicia.class));
    }

    @Override
    public Mono<Franquicia> guardar(Franquicia franquicia) {
        return save(franquicia);
    }

    @Override
    public Mono<Boolean> existePorTipoDocumentoYNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento) {
        return repository.existePorTipoDocumentoYNumeroDocumento(tipoDocumento, numeroDocumento);
    }
}
