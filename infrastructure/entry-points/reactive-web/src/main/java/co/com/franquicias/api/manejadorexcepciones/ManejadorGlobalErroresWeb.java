package co.com.franquicias.api.manejadorexcepciones;

import co.com.franquicias.api.dto.response.ErrorRespuesta;
import co.com.franquicias.model.exception.ExcepcionNegocio;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.Instant;


@Component
@Order(-2)
public class ManejadorGlobalErroresWeb extends AbstractErrorWebExceptionHandler {

    public ManejadorGlobalErroresWeb(ErrorAttributes errorAttributes,
                                      WebProperties.Resources resources,
                                      ApplicationContext applicationContext,
                                      ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderizarError);
    }

    private Mono<ServerResponse> renderizarError(ServerRequest request) {
        Throwable error = getError(request);
        HttpStatus status = resolverEstadoHttp(error);

        ErrorRespuesta body = ErrorRespuesta.builder()
                .fecha(Instant.now())
                .estado(status.value())
                .error(status.getReasonPhrase())
                .mensaje(error.getMessage())
                .ruta(request.path())
                .build();

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body);
    }

    private HttpStatus resolverEstadoHttp(Throwable error) {
        if (error instanceof ExcepcionNegocio excepcionNegocio) {
            return MapeoErrorHttp.paraCodigo(excepcionNegocio.getCodigo());
        }
        if (error instanceof ServerWebInputException || error instanceof IllegalArgumentException) {
            return MapeoErrorHttp.PETICION_INVALIDA.getHttpStatus();
        }
        return MapeoErrorHttp.ERROR_TECNICO.getHttpStatus();
    }
}
