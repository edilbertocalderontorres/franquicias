package co.com.franquicias.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPeticion {

    private String nombre;
    private String codigo;
    private Integer stock;
}
