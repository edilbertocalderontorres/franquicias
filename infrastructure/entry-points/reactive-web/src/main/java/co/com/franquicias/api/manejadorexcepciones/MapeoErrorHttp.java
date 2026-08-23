package co.com.franquicias.api.manejadorexcepciones;

import co.com.franquicias.model.exception.ErrorDominio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Slf4j
public enum MapeoErrorHttp {

    FRANQUICIA_NO_ENCONTRADA(ErrorDominio.FRANQUICIA_NO_ENCONTRADA.getCodigo(), HttpStatus.NOT_FOUND),
    FRANQUICIA_YA_EXISTE(ErrorDominio.FRANQUICIA_YA_EXISTE.getCodigo(), HttpStatus.CONFLICT),
    SUCURSAL_NO_ENCONTRADA(ErrorDominio.SUCURSAL_NO_ENCONTRADA.getCodigo(), HttpStatus.NOT_FOUND),
    SUCURSAL_YA_EXISTE(ErrorDominio.SUCURSAL_YA_EXISTE.getCodigo(), HttpStatus.CONFLICT),
    PRODUCTO_NO_ENCONTRADO(ErrorDominio.PRODUCTO_NO_ENCONTRADO.getCodigo(), HttpStatus.NOT_FOUND),
    PRODUCTO_YA_EXISTE(ErrorDominio.PRODUCTO_YA_EXISTE.getCodigo(), HttpStatus.CONFLICT),
    STOCK_INSUFICIENTE(ErrorDominio.STOCK_INSUFICIENTE.getCodigo(), HttpStatus.CONFLICT),
    PETICION_INVALIDA("PETICION_INVALIDA", HttpStatus.BAD_REQUEST),
    ERROR_TECNICO("ERROR_TECNICO", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String codigo;
    private final HttpStatus httpStatus;

    MapeoErrorHttp(String codigo, HttpStatus httpStatus) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static HttpStatus paraCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(mapeo -> mapeo.codigo.equals(codigo))
                .map(MapeoErrorHttp::getHttpStatus)
                .findFirst()
                .orElseGet(() -> {
                    log.warn("No existe equivalencia HTTP para el código de error de dominio '{}'; se usa {} por defecto",
                            codigo, ERROR_TECNICO.httpStatus.value());
                    return ERROR_TECNICO.httpStatus;
                });
    }
}
