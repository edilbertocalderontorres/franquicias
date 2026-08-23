package co.com.franquicias.model.sucursal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private Long id;
    private String nombre;
    private Long franquiciaId;
}
