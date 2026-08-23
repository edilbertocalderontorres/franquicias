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
@Table("producto")
public class ProductoEntidad {

    @Id
    private Long id;

    private String nombre;

    private Integer stock;

    @Column("sucursal_id")
    private Long sucursalId;
}
