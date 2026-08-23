package co.com.franquicias.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMayorStockPorSucursalRespuesta {

    private Long sucursalId;
    private String sucursalNombre;
    private Long productoId;
    private String productoNombre;
    private Integer stock;
}
