package co.com.franquicias.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranquiciaPeticion {

    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
}
