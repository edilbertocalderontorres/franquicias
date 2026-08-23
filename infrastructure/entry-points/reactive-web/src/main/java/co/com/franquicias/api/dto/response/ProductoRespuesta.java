package co.com.franquicias.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRespuesta {

    private Long id;
    private String nombre;
    private String codigo;
    private Integer stock;
    private Long sucursalId;
}
