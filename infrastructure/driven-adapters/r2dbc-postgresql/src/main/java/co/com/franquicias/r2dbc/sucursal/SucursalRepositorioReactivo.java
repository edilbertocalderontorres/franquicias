package co.com.franquicias.r2dbc.sucursal;

import co.com.franquicias.r2dbc.entidades.SucursalEntidad;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepositorioReactivo extends
        ReactiveCrudRepository<SucursalEntidad, Long>,
        ReactiveQueryByExampleExecutor<SucursalEntidad> {

    @Query("SELECT EXISTS(SELECT 1 FROM sucursal WHERE franquicia_id = :franquiciaId AND codigo = :codigo)")
    Mono<Boolean> existePorFranquiciaIdYCodigo(@Param("franquiciaId") Long franquiciaId, @Param("codigo") String codigo);

    @Query("SELECT * FROM sucursal WHERE franquicia_id = :franquiciaId")
    Flux<SucursalEntidad> buscarPorFranquiciaId(@Param("franquiciaId") Long franquiciaId);
}
