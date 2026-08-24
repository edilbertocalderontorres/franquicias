package co.com.franquicias.r2dbc.producto;

import co.com.franquicias.r2dbc.entidades.ProductoEntidad;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductoRepositorioReactivo extends
        ReactiveCrudRepository<ProductoEntidad, Long>,
        ReactiveQueryByExampleExecutor<ProductoEntidad> {

    @Query("SELECT EXISTS(SELECT 1 FROM producto WHERE sucursal_id = :sucursalId AND codigo = :codigo)")
    Mono<Boolean> existePorSucursalIdYCodigo(@Param("sucursalId") Long sucursalId, @Param("codigo") String codigo);

    @Query("SELECT * FROM producto WHERE id = :id AND activo = true")
    Mono<ProductoEntidad> buscarActivoPorId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE producto SET activo = false WHERE id = :id")
    Mono<Void> eliminarLogicamente(@Param("id") Long id);

    @Modifying
    @Query("UPDATE producto SET stock = stock + :delta WHERE id = :id AND activo = true AND stock + :delta >= 0")
    Mono<Integer> aplicarDelta(@Param("id") Long id, @Param("delta") Integer delta);

    @Query("SELECT * FROM producto WHERE sucursal_id = :sucursalId AND activo = true ORDER BY stock DESC LIMIT 1")
    Mono<ProductoEntidad> buscarMayorStockPorSucursal(@Param("sucursalId") Long sucursalId);
}
