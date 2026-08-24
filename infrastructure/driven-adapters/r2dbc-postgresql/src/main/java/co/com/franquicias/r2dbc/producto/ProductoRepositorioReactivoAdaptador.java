package co.com.franquicias.r2dbc.producto;

import co.com.franquicias.model.exception.ErrorDominio;
import co.com.franquicias.model.exception.ExcepcionNegocio;
import co.com.franquicias.model.exception.ExcepcionNoEncontrada;
import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import co.com.franquicias.r2dbc.entidades.ProductoEntidad;
import co.com.franquicias.r2dbc.entidades.TransaccionInventarioEntidad;
import co.com.franquicias.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class ProductoRepositorioReactivoAdaptador extends ReactiveAdapterOperations<
        Producto,
        ProductoEntidad,
        Long,
        ProductoRepositorioReactivo
    > implements ProductoRepositorio {

    private final TransaccionInventarioRepositorioReactivo transaccionInventarioRepositorioReactivo;
    private final TransactionalOperator transactionalOperator;

    public ProductoRepositorioReactivoAdaptador(ProductoRepositorioReactivo repository,
                                                 TransaccionInventarioRepositorioReactivo transaccionInventarioRepositorioReactivo,
                                                 TransactionalOperator transactionalOperator,
                                                 ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Producto.class));
        this.transaccionInventarioRepositorioReactivo = transaccionInventarioRepositorioReactivo;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Producto> guardar(Producto producto) {
        return save(producto);
    }

    @Override
    public Mono<Producto> buscarPorId(Long id) {
        return repository.buscarActivoPorId(id).map(this::toEntity);
    }

    @Override
    public Mono<Void> eliminarLogicamente(Long id) {
        return repository.eliminarLogicamente(id);
    }

    @Override
    public Mono<Boolean> existePorSucursalIdYCodigo(Long sucursalId, String codigo) {
        return repository.existePorSucursalIdYCodigo(sucursalId, codigo);
    }

    @Override
    public Mono<Producto> aplicarMovimientoStock(Long productoId, String idempotencyKey, String usuario, Integer delta) {
        return transaccionInventarioRepositorioReactivo.existePorIdkey(idempotencyKey)
                .flatMap(yaProcesada -> yaProcesada
                        ? repository.buscarActivoPorId(productoId)
                        : aplicarDeltaYRegistrar(productoId, idempotencyKey, usuario, delta)
                                .onErrorResume(DuplicateKeyException.class,
                                        error -> repository.buscarActivoPorId(productoId)))
                .switchIfEmpty(Mono.error(new ExcepcionNoEncontrada(ErrorDominio.PRODUCTO_NO_ENCONTRADO)))
                .map(this::toEntity);
    }

    @Override
    public Mono<Producto> buscarMayorStockPorSucursal(Long sucursalId) {
        return repository.buscarMayorStockPorSucursal(sucursalId).map(this::toEntity);
    }

    private Mono<ProductoEntidad> aplicarDeltaYRegistrar(Long productoId, String idempotencyKey, String usuario, Integer delta) {
        Mono<ProductoEntidad> operacion = repository.aplicarDelta(productoId, delta)
                .filter(filasAfectadas -> filasAfectadas > 0)
                .switchIfEmpty(Mono.error(new ExcepcionNegocio(ErrorDominio.STOCK_INSUFICIENTE)))
                .then(transaccionInventarioRepositorioReactivo.save(TransaccionInventarioEntidad.builder()
                        .idkey(idempotencyKey)
                        .productoId(productoId)
                        .delta(delta)
                        .usuario(usuario)
                        .fechaCreacion(Instant.now())
                        .build()))
                .then(repository.buscarActivoPorId(productoId));
        return transactionalOperator.transactional(operacion);
    }
}
