package co.com.franquicias.model.sucursal.gateways;

import co.com.franquicias.model.sucursal.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepositorio {

    Mono<Sucursal> guardar(Sucursal sucursal);

    Mono<Sucursal> buscarPorId(Long id);

    Flux<Sucursal> buscarPorFranquiciaId(Long franquiciaId);

    Mono<Boolean> existePorFranquiciaIdYCodigo(Long franquiciaId, String codigo);
}
