package co.com.franquicias.model.sucursal.gateways;

import co.com.franquicias.model.sucursal.Sucursal;
import reactor.core.publisher.Mono;

public interface SucursalRepositorio {

    Mono<Sucursal> guardar(Sucursal sucursal);

    Mono<Sucursal> buscarPorId(Long id);

    Mono<Boolean> existePorFranquiciaIdYCodigo(Long franquiciaId, String codigo);
}
