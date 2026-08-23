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
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/franquicias"), handler::crearFranquicia)
                .andRoute(POST("/api/franquicias/{franquiciaId}/sucursales"), handler::crearSucursal)
                .andRoute(POST("/api/sucursales/{sucursalId}/productos"), handler::crearProducto)
                .andRoute(DELETE("/api/sucursales/{sucursalId}/productos/{productoId}"), handler::eliminarProducto)
                .andRoute(PATCH("/api/productos/{productoId}/stock"), handler::actualizarStockProducto)
                .andRoute(GET("/api/franquicias/{franquiciaId}/productos/mayor-stock"), handler::consultarProductoMayorStockPorSucursal);
    }
}
