package co.com.franquicias.api;

import co.com.franquicias.api.dto.request.ActualizarNombrePeticion;
import co.com.franquicias.api.dto.request.SucursalPeticion;
import co.com.franquicias.api.mapper.SucursalMapper;
import co.com.franquicias.usecase.sucursales.ActualizarNombreSucursalCasoDeUso;
import co.com.franquicias.usecase.sucursales.CrearSucursalPorFranquiciaCasoDeUso;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ManejadorSucursal {

    private final CrearSucursalPorFranquiciaCasoDeUso crearSucursalPorFranquiciaCasoDeUso;
    private final ActualizarNombreSucursalCasoDeUso actualizarNombreSucursalCasoDeUso;
    private final SucursalMapper sucursalMapper;

    public Mono<ServerResponse> crear(ServerRequest request) {
        Long franquiciaId = Long.valueOf(request.pathVariable("franquiciaId"));
        return request.bodyToMono(SucursalPeticion.class)
                .map(sucursalMapper::aDominio)
                .doOnNext(sucursal -> sucursal.setFranquiciaId(franquiciaId))
                .flatMap(crearSucursalPorFranquiciaCasoDeUso::ejecutar)
                .map(sucursalMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.CREATED).bodyValue(respuesta));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return request.bodyToMono(ActualizarNombrePeticion.class)
                .map(ActualizarNombrePeticion::getNombre)
                .flatMap(nombre -> actualizarNombreSucursalCasoDeUso.ejecutar(sucursalId, nombre))
                .map(sucursalMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.OK).bodyValue(respuesta));
    }
}
