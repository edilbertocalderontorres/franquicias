package co.com.franquicias.r2dbc.producto;

import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.producto.gateways.ProductoRepositorio;
import co.com.franquicias.r2dbc.entity.ProductoEntidad;
import co.com.franquicias.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductoRepositorioReactivoAdaptador extends ReactiveAdapterOperations<
        Producto,
        ProductoEntidad,
        Long,
        ProductoRepositorioReactivo
    > implements ProductoRepositorio {

    public ProductoRepositorioReactivoAdaptador(ProductoRepositorioReactivo repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Producto.class));
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
}
