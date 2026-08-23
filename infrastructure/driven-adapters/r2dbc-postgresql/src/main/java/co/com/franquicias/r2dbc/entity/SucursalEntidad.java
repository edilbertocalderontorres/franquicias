package co.com.franquicias.r2dbc.entity;

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
@Table("sucursal")
public class SucursalEntidad {

    @Id
    private Long id;

    private String nombre;

    @Column("franquicia_id")
    private Long franquiciaId;
}
