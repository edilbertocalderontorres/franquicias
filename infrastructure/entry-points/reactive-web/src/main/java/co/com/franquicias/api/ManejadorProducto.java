package co.com.franquicias.api;

import co.com.franquicias.api.dto.request.ActualizarNombrePeticion;
import co.com.franquicias.api.dto.request.ActualizarStockPeticion;
import co.com.franquicias.api.dto.request.ProductoPeticion;
import co.com.franquicias.api.mapper.ProductoMapper;
import co.com.franquicias.usecase.consultasnegocio.ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;
import co.com.franquicias.usecase.inventario.ActualizarInventarioProductoCasoDeUso;
import co.com.franquicias.usecase.productos.ActualizarNombreProductoCasoDeUso;
import co.com.franquicias.usecase.productos.BorrarProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.productos.CrearProductoPorSucursalCasoDeUso;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ManejadorProducto {

    private final CrearProductoPorSucursalCasoDeUso crearProductoPorSucursalCasoDeUso;
    private final BorrarProductoPorSucursalCasoDeUso borrarProductoPorSucursalCasoDeUso;
    private final ActualizarInventarioProductoCasoDeUso actualizarInventarioProductoCasoDeUso;
    private final ActualizarNombreProductoCasoDeUso actualizarNombreProductoCasoDeUso;
    private final ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso consultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;
    private final ProductoMapper productoMapper;

    public Mono<ServerResponse> crear(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return request.bodyToMono(ProductoPeticion.class)
                .map(productoMapper::aDominio)
                .doOnNext(producto -> producto.setSucursalId(sucursalId))
                .flatMap(crearProductoPorSucursalCasoDeUso::ejecutar)
                .map(productoMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.CREATED).bodyValue(respuesta));
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return borrarProductoPorSucursalCasoDeUso.ejecutar(sucursalId, productoId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> actualizarStock(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        String idempotencyKey = obtenerHeaderRequerido(request, "Idempotency-Key");
        String usuario = obtenerHeaderRequerido(request, "X-Usuario");
        return request.bodyToMono(ActualizarStockPeticion.class)
                .map(ActualizarStockPeticion::getDelta)
                .flatMap(delta -> actualizarInventarioProductoCasoDeUso.ejecutar(productoId, idempotencyKey, usuario, delta))
                .map(productoMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.OK).bodyValue(respuesta));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ActualizarNombrePeticion.class)
                .map(ActualizarNombrePeticion::getNombre)
                .flatMap(nombre -> actualizarNombreProductoCasoDeUso.ejecutar(productoId, nombre))
                .map(productoMapper::aRespuesta)
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.OK).bodyValue(respuesta));
    }

    public Mono<ServerResponse> consultarMayorStockPorSucursal(ServerRequest request) {
        Long franquiciaId = Long.valueOf(request.pathVariable("franquiciaId"));
        return consultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso.ejecutar(franquiciaId)
                .map(tupla -> productoMapper.aRespuesta(tupla.getT1(), tupla.getT2()))
                .collectList()
                .flatMap(respuesta -> ServerResponse.status(HttpStatus.OK).bodyValue(respuesta));
    }

    private String obtenerHeaderRequerido(ServerRequest request, String nombre) {
        String valor = request.headers().firstHeader(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Falta el header requerido: " + nombre);
        }
        return valor;
    }
}
