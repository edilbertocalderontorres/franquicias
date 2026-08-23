package co.com.franquicias.r2dbc.franquicia;

import co.com.franquicias.model.franquicia.TipoDocumento;
import co.com.franquicias.r2dbc.entity.FranquiciaEntidad;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositorioReactivo extends
        ReactiveCrudRepository<FranquiciaEntidad, Long>,
        ReactiveQueryByExampleExecutor<FranquiciaEntidad> {

    @Query("SELECT EXISTS(SELECT 1 FROM franquicia WHERE tipo_documento = :tipoDocumento AND numero_documento = :numeroDocumento)")
    Mono<Boolean> existePorTipoDocumentoYNumeroDocumento(@Param("tipoDocumento") TipoDocumento tipoDocumento,
                                                          @Param("numeroDocumento") String numeroDocumento);
}
