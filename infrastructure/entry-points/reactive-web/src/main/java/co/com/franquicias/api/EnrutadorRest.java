package co.com.franquicias.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class EnrutadorRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ManejadorFranquicia manejadorFranquicia,
                                                           ManejadorSucursal manejadorSucursal,
                                                           ManejadorProducto manejadorProducto) {
        return route(POST("/api/franquicias"), manejadorFranquicia::crear)
                .andRoute(PATCH("/api/franquicias/{franquiciaId}/nombre"), manejadorFranquicia::actualizarNombre)
                .andRoute(POST("/api/franquicias/{franquiciaId}/sucursales"), manejadorSucursal::crear)
                .andRoute(PATCH("/api/sucursales/{sucursalId}/nombre"), manejadorSucursal::actualizarNombre)
                .andRoute(POST("/api/sucursales/{sucursalId}/productos"), manejadorProducto::crear)
                .andRoute(DELETE("/api/sucursales/{sucursalId}/productos/{productoId}"), manejadorProducto::eliminar)
                .andRoute(PATCH("/api/productos/{productoId}/stock"), manejadorProducto::actualizarStock)
                .andRoute(PATCH("/api/productos/{productoId}/nombre"), manejadorProducto::actualizarNombre)
                .andRoute(GET("/api/franquicias/{franquiciaId}/productos/mayor-stock"), manejadorProducto::consultarMayorStockPorSucursal);
    }
}
