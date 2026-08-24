package co.com.franquicias.r2dbc.sucursal;

import co.com.franquicias.model.sucursal.Sucursal;
import co.com.franquicias.model.sucursal.gateways.SucursalRepositorio;
import co.com.franquicias.r2dbc.entidades.SucursalEntidad;
import co.com.franquicias.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SucursalRepositorioReactivoAdaptador extends ReactiveAdapterOperations<
        Sucursal,
        SucursalEntidad,
        Long,
        SucursalRepositorioReactivo
    > implements SucursalRepositorio {

    public SucursalRepositorioReactivoAdaptador(SucursalRepositorioReactivo repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Sucursal.class));
    }

    @Override
    public Mono<Sucursal> guardar(Sucursal sucursal) {
        return save(sucursal);
    }

    @Override
    public Mono<Sucursal> buscarPorId(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Sucursal> buscarPorFranquiciaId(Long franquiciaId) {
        return repository.buscarPorFranquiciaId(franquiciaId).map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existePorFranquiciaIdYCodigo(Long franquiciaId, String codigo) {
        return repository.existePorFranquiciaIdYCodigo(franquiciaId, codigo);
    }
}
