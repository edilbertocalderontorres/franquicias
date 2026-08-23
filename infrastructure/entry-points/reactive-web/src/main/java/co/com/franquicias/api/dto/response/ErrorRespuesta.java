package co.com.franquicias.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRespuesta {

    private Instant fecha;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;
}
