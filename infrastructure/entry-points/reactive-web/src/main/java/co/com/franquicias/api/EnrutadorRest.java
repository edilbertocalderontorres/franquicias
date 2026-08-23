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
    public RouterFunction<ServerResponse> routerFunction(ManejadorWeb manejadorWeb) {
        return route(POST("/api/franquicias"), manejadorWeb::crearFranquicia)
                .andRoute(POST("/api/franquicias/{franquiciaId}/sucursales"), manejadorWeb::crearSucursal)
                .andRoute(POST("/api/sucursales/{sucursalId}/productos"), manejadorWeb::crearProducto)
                .andRoute(DELETE("/api/sucursales/{sucursalId}/productos/{productoId}"), manejadorWeb::eliminarProducto)
                .andRoute(PATCH("/api/productos/{productoId}/stock"), manejadorWeb::actualizarStockProducto)
                .andRoute(GET("/api/franquicias/{franquiciaId}/productos/mayor-stock"), manejadorWeb::consultarProductoMayorStockPorSucursal);
    }
}
