package co.com.franquicias.api;

import co.com.franquicias.usecase.consultasnegocio.ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;
import co.com.franquicias.usecase.inventario.ActualizarInventarioProductoCasoDeUso;
import co.com.franquicias.usecase.franquicias.CrearFranquiciaCasoDeUso;
import co.com.franquicias.usecase.productos.BorrarProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.productos.CrearProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.sucursales.CrearSucursalPorFranquiciaCasoDeUso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CrearFranquiciaCasoDeUso crearFranquiciaCasoDeUso;
    private final CrearSucursalPorFranquiciaCasoDeUso crearSucursalPorFranquiciaCasoDeUso;
    private final CrearProductoPorSucursalCasoDeUso crearProductoPorSucursalCasoDeUso;
    private final BorrarProductoPorSucursalCasoDeUso borrarProductoPorSucursalCasoDeUso;
    private final ActualizarInventarioProductoCasoDeUso actualizarInventarioProductoCasoDeUso;
    private final ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso consultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;

    public Mono<ServerResponse> crearFranquicia(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> crearSucursal(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> crearProducto(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> eliminarProducto(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> actualizarStockProducto(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> consultarProductoMayorStockPorSucursal(ServerRequest request) {
        return null;
    }
}
