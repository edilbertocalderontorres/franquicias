package co.com.franquicias.r2dbc.producto;

import co.com.franquicias.r2dbc.entidades.TransaccionInventarioEntidad;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TransaccionInventarioRepositorioReactivo extends
        ReactiveCrudRepository<TransaccionInventarioEntidad, Long> {

    @Query("SELECT EXISTS(SELECT 1 FROM transaccion_inventario WHERE idkey = :idkey)")
    Mono<Boolean> existePorIdkey(@Param("idkey") String idkey);
}
