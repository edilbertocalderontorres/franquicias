package co.com.franquicias.model.franquicia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franquicia {

    private Long id;
    private String nombre;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
}
