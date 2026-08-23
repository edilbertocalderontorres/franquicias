package co.com.franquicias.api.config;

import co.com.franquicias.api.Handler;
import co.com.franquicias.api.RouterRest;
import co.com.franquicias.usecase.consultasnegocio.ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;
import co.com.franquicias.usecase.franquicias.CrearFranquiciaCasoDeUso;
import co.com.franquicias.usecase.inventario.ActualizarInventarioProductoCasoDeUso;
import co.com.franquicias.usecase.productos.BorrarProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.productos.CrearProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.sucursales.CrearSucursalPorFranquiciaCasoDeUso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CrearFranquiciaCasoDeUso crearFranquiciaCasoDeUso;

    @MockitoBean
    private CrearSucursalPorFranquiciaCasoDeUso crearSucursalPorFranquiciaCasoDeUso;

    @MockitoBean
    private CrearProductoPorSucursalCasoDeUso crearProductoPorSucursalCasoDeUso;

    @MockitoBean
    private BorrarProductoPorSucursalCasoDeUso borrarProductoPorSucursalCasoDeUso;

    @MockitoBean
    private ActualizarInventarioProductoCasoDeUso actualizarInventarioProductoCasoDeUso;

    @MockitoBean
    private ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso consultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/no-existe")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().doesNotExist("Server")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}