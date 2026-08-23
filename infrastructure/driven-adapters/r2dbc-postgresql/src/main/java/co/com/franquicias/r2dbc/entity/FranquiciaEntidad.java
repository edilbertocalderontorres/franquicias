package co.com.franquicias.r2dbc.entity;

import co.com.franquicias.model.franquicia.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("franquicia")
public class FranquiciaEntidad {

    @Id
    private Long id;

    private String nombre;

    @Column("tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column("numero_documento")
    private String numeroDocumento;
}
