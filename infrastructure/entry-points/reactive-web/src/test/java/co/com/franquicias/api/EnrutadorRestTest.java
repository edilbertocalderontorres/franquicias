package co.com.franquicias.api;

import co.com.franquicias.api.mapper.FranquiciaMapper;
import co.com.franquicias.api.mapper.SucursalMapper;
import co.com.franquicias.usecase.consultasnegocio.ConsultaProductoMayorStockPorSucursalDeFranquiciaCasoDeUso;
import co.com.franquicias.usecase.franquicias.CrearFranquiciaCasoDeUso;
import co.com.franquicias.usecase.inventario.ActualizarInventarioProductoCasoDeUso;
import co.com.franquicias.usecase.productos.BorrarProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.productos.CrearProductoPorSucursalCasoDeUso;
import co.com.franquicias.usecase.sucursales.CrearSucursalPorFranquiciaCasoDeUso;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {EnrutadorRest.class, ManejadorWeb.class})
@WebFluxTest
class EnrutadorRestTest {

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

    @MockitoBean
    private FranquiciaMapper franquiciaMapper;

    @MockitoBean
    private SucursalMapper sucursalMapper;

    @Test
    void debeCargarElContextoConLasRutasDefinidas() {
        Assertions.assertThat(webTestClient).isNotNull();
    }
}
