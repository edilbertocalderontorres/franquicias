package co.com.franquicias.api;

import co.com.franquicias.api.dto.request.ActualizarNombrePeticion;
import co.com.franquicias.api.dto.request.FranquiciaPeticion;
import co.com.franquicias.api.mapper.FranquiciaMapper;
import co.com.franquicias.usecase.franquicias.ActualizarNombreFranquiciaCasoDeUso;
import co.com.franquicias.usecase.franquicias.CrearFranquiciaCasoDeUso;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ManejadorFranquicia {

    private final CrearFranquiciaCasoDeUso crearFranquiciaCasoDeUso;
    private final ActualizarNombreFranquiciaCasoDeUso actualizarNombreFranquiciaCasoDeUso;
    private final FranquiciaMapper franquiciaMapper;

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(FranquiciaPeticion.class)
                .map(franquiciaMapper::aDominio)
                .flatMap(crearFranquiciaCasoDeUso::ejecutar)
                .map(franquiciaMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.CREATED).bodyValue(respuesta));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        Long franquiciaId = Long.valueOf(request.pathVariable("franquiciaId"));
        return request.bodyToMono(ActualizarNombrePeticion.class)
                .map(ActualizarNombrePeticion::getNombre)
                .flatMap(nombre -> actualizarNombreFranquiciaCasoDeUso.ejecutar(franquiciaId, nombre))
                .map(franquiciaMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.OK).bodyValue(respuesta));
    }
}
